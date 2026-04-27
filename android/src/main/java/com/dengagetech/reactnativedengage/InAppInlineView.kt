package com.dengagetech.reactnativedengage

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.FrameLayout
import com.dengage.sdk.Dengage
import com.dengage.sdk.ui.inappmessage.InAppInlineElement
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter

class InAppInlineView(private val reactContext: ThemedReactContext) : FrameLayout(reactContext) {

    val inlineElement = InAppInlineElement(context)
    var propertyId: String? = null
    var screenName: String? = null
    var customParams: HashMap<String, String>? = null
    var hideIfNotFound: Boolean = true
    var hasShownInline = false
    private var lastNavigationKey: String? = null
    /** Elapsed realtime when [Dengage.showInlineInApp] was last invoked; 0 = not yet this session. */
    private var lastShowInlineInvokedAtElapsed: Long = 0L

    private val handler = Handler(Looper.getMainLooper())
    private var lastReportedHidden: Boolean? = null
    private var hiddenSinceElapsed: Long? = null
    private var disposed = false

    /**
     * Android native SDK delays inline populate via [java.util.Timer] then [Activity.runOnUiThread].
     * If RN unmounts this view first, [InAppInlineElement.destroy] would run before that runnable,
     * causing "Application attempted to call on a destroyed WebView". We cancel the SDK timer
     * via [Dengage.removeInAppMessageDisplay] and defer destroy so any already-queued populate runs first.
     */
    private val destroyWebViewRunnable = Runnable {
        runCatching { inlineElement.destroy() }
    }

    /**
     * RN sets props in quick succession; calling [Dengage.showInlineInApp] only on the first setter
     * often runs before [ThemedReactContext.currentActivity] or the view hierarchy is ready. We also
     * used to skip all later SDK calls when [lastNavigationKey] was unchanged, so the only recovery
     * was changing propertyId or remounting. Coalesce to one invoke after props settle.
     */
    private val debouncedSdkShowRunnable = Runnable {
        if (disposed) return@Runnable
        val pid = propertyId?.trim().orEmpty()
        val screen = screenName?.trim().orEmpty()
        val params = customParams ?: HashMap()
        val act = reactContext.currentActivity
        if (pid.isEmpty() || screen.isEmpty() || act == null) return@Runnable

        inlineElement.visibility = View.VISIBLE
        inlineElement.alpha = 1f
        ensureInlineFillsSlot()
        lastShowInlineInvokedAtElapsed = SystemClock.elapsedRealtime()
        Dengage.showInlineInApp(
            propertyId = pid,
            inAppInlineElement = inlineElement,
            activity = act,
            customParams = params,
            screenName = screen,
            hideIfNotFound = hideIfNotFound,
        )
    }

    private fun inlineAppearsHiddenForReporting(): Boolean {
        val v = inlineElement
        if (v.visibility != View.VISIBLE) return true
        if (v.alpha < 0.02f) return true
        return false
    }

    private val visibilityPoll = object : Runnable {
        override fun run() {
            if (disposed) return

            val notVisible = inlineAppearsHiddenForReporting()
            val now = SystemClock.elapsedRealtime()
            val debouncedHidden = if (notVisible) {
                if (hiddenSinceElapsed == null) hiddenSinceElapsed = now
                (now - hiddenSinceElapsed!!) >= HIDDEN_DEBOUNCE_MS
            } else {
                hiddenSinceElapsed = null
                false
            }

            // With hideIfNotFound, the Android SDK may keep the WebView GONE briefly while loading; do not
            // report hidden until grace elapses or JS will unmount and kill the WebView before HTML loads.
            // While lastShowInlineInvokedAtElapsed == 0, [debouncedSdkShowRunnable] has not run yet (it is
            // posted with DEBOUNCE_SDK_SHOW_MS); reporting hidden here made JS collapse before show().
            val hiddenForEvent = when {
                !debouncedHidden -> false
                !hideIfNotFound -> debouncedHidden
                lastShowInlineInvokedAtElapsed == 0L -> false
                (now - lastShowInlineInvokedAtElapsed) < INLINE_SHOW_GRACE_MS -> false
                else -> true
            }

            if (lastReportedHidden == null || lastReportedHidden != hiddenForEvent) {
                lastReportedHidden = hiddenForEvent
                emitVisibilityChanged(hiddenForEvent)
            }

            handler.postDelayed(this, POLL_MS)
        }
    }

    init {
        addView(inlineElement, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post { maybeShowInlineInApp() }
    }

    /**
     * The Dengage SDK replaces [WebView.webViewClient] when loading HTML. Our old client set a tiny
     * height from scrollHeight on an empty page; that height stuck while RN still applied minHeight,
     * so the slot looked "occupied" but the WebView stayed effectively invisible.
     */
    private fun ensureInlineFillsSlot() {
        val lp = inlineElement.layoutParams as? LayoutParams
            ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        lp.width = LayoutParams.MATCH_PARENT
        lp.height = LayoutParams.MATCH_PARENT
        inlineElement.layoutParams = lp
    }

    fun maybeShowInlineInApp() {
        if (disposed) return

        val currentPropertyId = propertyId?.trim().orEmpty()
        val currentScreenName = screenName?.trim().orEmpty()
        val currentCustomParams = customParams ?: HashMap()

        // Only empty ids mean "no slot" from JS. SDK invoke is debounced — [debouncedSdkShowRunnable]
        // re-reads [reactContext.currentActivity] so we still show after the Activity attaches.
        if (currentPropertyId.isEmpty() || currentScreenName.isEmpty()) {
            handler.removeCallbacks(debouncedSdkShowRunnable)
            handler.removeCallbacks(visibilityPoll)
            lastNavigationKey = null
            hasShownInline = false
            lastShowInlineInvokedAtElapsed = 0L
            if (hideIfNotFound) {
                inlineElement.visibility = View.GONE
                lastReportedHidden = null
                hiddenSinceElapsed = null
                emitVisibilityChanged(true)
            }
            return
        }

        val navigationKey = navigationKey(
            currentPropertyId,
            currentScreenName,
            currentCustomParams,
            hideIfNotFound
        )

        val keyChanged = navigationKey != lastNavigationKey
        if (!hasShownInline || keyChanged) {
            lastNavigationKey = navigationKey
            lastReportedHidden = null
            hiddenSinceElapsed = null
            hasShownInline = true
            startVisibilityPolling()
        }

        handler.removeCallbacks(debouncedSdkShowRunnable)
        handler.postDelayed(debouncedSdkShowRunnable, DEBOUNCE_SDK_SHOW_MS)
    }

    private fun navigationKey(
        pid: String,
        screen: String,
        params: HashMap<String, String>,
        hide: Boolean
    ): String {
        val sorted = params.entries.sortedBy { it.key }.joinToString(",") { "${it.key}=${it.value}" }
        return "$pid|$screen|$hide|$sorted"
    }

    private fun startVisibilityPolling() {
        handler.removeCallbacks(visibilityPoll)
        lastReportedHidden = null
        hiddenSinceElapsed = null
        handler.post(visibilityPoll)
    }

    private fun emitVisibilityChanged(isHidden: Boolean) {
        fun send() {
            if (id == NO_ID) return
            val map = Arguments.createMap()
            map.putBoolean("isHidden", isHidden)
            @Suppress("DEPRECATION")
            (context as ReactContext).getJSModule(RCTEventEmitter::class.java)
                .receiveEvent(id, "topVisibilityChanged", map)
        }
        if (id == NO_ID) {
            post { send() }
        } else {
            send()
        }
    }

    fun dispose() {
        if (disposed) return
        disposed = true
        handler.removeCallbacks(debouncedSdkShowRunnable)
        handler.removeCallbacks(visibilityPoll)
        handler.removeCallbacks(destroyWebViewRunnable)
        Dengage.removeInAppMessageDisplay()
        handler.postDelayed(destroyWebViewRunnable, WEBVIEW_DESTROY_DELAY_MS)
    }

    private companion object {
        const val POLL_MS = 50L
        const val HIDDEN_DEBOUNCE_MS = 0L
        /** Allow SDK main-thread populate to run before [WebView.destroy] (see [destroyWebViewRunnable]). */
        const val WEBVIEW_DESTROY_DELAY_MS = 200L
        /** Do not emit JS "hidden" during this window after show — avoids unmount-before-load when hideIfNotFound. */
        const val INLINE_SHOW_GRACE_MS = 550L
        /** Wait for RN props + Activity before invoking the SDK (see [debouncedSdkShowRunnable]). */
        const val DEBOUNCE_SDK_SHOW_MS = 100L
    }
}
