package com.dengagetech.reactnativedengage

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.dengage.sdk.Dengage
import com.dengage.sdk.ui.inappmessage.InAppInlineElement
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper

class InAppInlineView(context: Context) : FrameLayout(context) {
    val inlineElement = InAppInlineElement(context)

    var reactPropertyId: String? = null
    var reactScreenName: String? = null
    var reactCustomParams: HashMap<String, String>? = null
    var reactHideIfNotFound: Boolean = true

    private var lastAppliedConfigurationSignature: String? = null

    private val applyConfigurationRunnable = Runnable { applyInlineConfigurationIfReady() }

    private val visibilityPollHandler = Handler(Looper.getMainLooper())
    private var lastSentIsHidden: Boolean? = null
    private var hiddenSinceElapsed: Long? = null

    private val visibilityPollRunnable = object : Runnable {
        override fun run() {
            if (!isAttachedToWindow) {
                return
            }
            val rawHidden = inlineElement.visibility != View.VISIBLE
            val now = SystemClock.elapsedRealtime()
            val debouncedHidden = if (rawHidden) {
                if (hiddenSinceElapsed == null) {
                    hiddenSinceElapsed = now
                }
                (now - (hiddenSinceElapsed ?: now)) >= HIDDEN_DEBOUNCE_MS
            } else {
                hiddenSinceElapsed = null
                false
            }
            if (lastSentIsHidden == null || lastSentIsHidden != debouncedHidden) {
                lastSentIsHidden = debouncedHidden
                dispatchInlineVisibility(debouncedHidden)
            }
            visibilityPollHandler.postDelayed(this, POLL_MS)
        }
    }

    private val performShowRunnable = Runnable {
        val themedContext = context as? ThemedReactContext ?: return@Runnable
        val activity = themedContext.currentActivity ?: return@Runnable
        val expected = lastAppliedConfigurationSignature ?: return@Runnable
        if (computeConfigurationSignature() != expected) {
            return@Runnable
        }
        Dengage.showInlineInApp(
            propertyId = reactPropertyId!!,
            inAppInlineElement = inlineElement,
            activity = activity,
            customParams = reactCustomParams,
            screenName = reactScreenName!!,
            hideIfNotFound = reactHideIfNotFound
        )
    }

    init {
        addView(inlineElement, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

        inlineElement.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                view?.evaluateJavascript(
                    "(function() { return document.body.scrollHeight; })();"
                ) { heightStr ->
                    val height = heightStr?.replace("\"", "")?.toIntOrNull() ?: 1
                    inlineElement.layoutParams?.height = height
                    inlineElement.requestLayout()
                    this@InAppInlineView.layoutParams?.height = height
                    this@InAppInlineView.requestLayout()
                }
            }
        }
    }

    fun cleanup() {
        visibilityPollHandler.removeCallbacks(visibilityPollRunnable)
        removeCallbacks(applyConfigurationRunnable)
        removeCallbacks(performShowRunnable)
    }

    fun scheduleApplyInlineConfiguration() {
        removeCallbacks(applyConfigurationRunnable)
        removeCallbacks(performShowRunnable)
        post(applyConfigurationRunnable)
    }

    private fun computeConfigurationSignature(): String? {
        val p = reactPropertyId ?: return null
        val s = reactScreenName ?: return null
        val c = reactCustomParams ?: return null
        val paramsPart =
            c.entries.sortedBy { it.key }.joinToString(separator = "\u0001") { "${it.key}=${it.value}" }
        return "$p\u0000$s\u0000$paramsPart\u0000${reactHideIfNotFound}"
    }

    private fun resetInlineWebContent() {
        lastSentIsHidden = null
        hiddenSinceElapsed = null
        inlineElement.stopLoading()
        inlineElement.loadUrl("about:blank")
        inlineElement.clearHistory()
        val elp = inlineElement.layoutParams as? LayoutParams
            ?: LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).also { inlineElement.layoutParams = it }
        elp.height = LayoutParams.MATCH_PARENT
        inlineElement.layoutParams = elp
        (layoutParams as? LayoutParams)?.let { plp ->
            plp.height = LayoutParams.WRAP_CONTENT
            layoutParams = plp
        }
        requestLayout()
    }

    private fun applyInlineConfigurationIfReady() {
        val signature = computeConfigurationSignature() ?: return
        if (signature == lastAppliedConfigurationSignature) {
            return
        }
        val reloading = lastAppliedConfigurationSignature != null
        if (reloading) {
            resetInlineWebContent()
        }
        lastAppliedConfigurationSignature = signature
        if (reloading) {
            post(performShowRunnable)
        } else {
            performShowRunnable.run()
        }
    }

    private fun dispatchInlineVisibility(isHidden: Boolean) {
        val themedContext = context as? ThemedReactContext ?: return
        val dispatcher =
            UIManagerHelper.getEventDispatcherForReactTag(themedContext, id) ?: return
        dispatcher.dispatchEvent(
            InlineVisibilityChangedEvent(
                UIManagerHelper.getSurfaceId(this),
                id,
                isHidden
            )
        )
    }

    private fun startVisibilityPolling() {
        visibilityPollHandler.removeCallbacks(visibilityPollRunnable)
        lastSentIsHidden = null
        hiddenSinceElapsed = null
        visibilityPollHandler.post(visibilityPollRunnable)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startVisibilityPolling()
    }

    override fun onDetachedFromWindow() {
        cleanup()
        super.onDetachedFromWindow()
    }

    companion object {
        private const val POLL_MS = 50L
        private const val HIDDEN_DEBOUNCE_MS = 0L
    }
}
