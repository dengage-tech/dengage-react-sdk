package com.dengagetech.reactnativedengage

import android.content.Context
import android.widget.FrameLayout
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dengage.sdk.Dengage
import com.dengage.sdk.ui.inappmessage.InAppInlineElement
import com.facebook.react.uimanager.ThemedReactContext

class InAppInlineView(context: Context) : FrameLayout(context) {
    val inlineElement = InAppInlineElement(context)

    var reactPropertyId: String? = null
    var reactScreenName: String? = null
    var reactCustomParams: HashMap<String, String>? = null

    private var lastAppliedConfigurationSignature: String? = null

    private val applyConfigurationRunnable = Runnable { applyInlineConfigurationIfReady() }

    private val performShowRunnable = Runnable {
        val themedContext = context as? ThemedReactContext ?: return@Runnable
        val activity = themedContext.currentActivity ?: return@Runnable
        val expected = lastAppliedConfigurationSignature ?: return@Runnable
        if (computeConfigurationSignature() != expected) {
            return@Runnable
        }
        Dengage.showInlineInApp(
            screenName = reactScreenName!!,
            inAppInlineElement = inlineElement,
            propertyId = reactPropertyId!!,
            activity = activity,
            customParams = reactCustomParams!!
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
        return "$p\u0000$s\u0000$paramsPart"
    }

    private fun resetInlineWebContent() {
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
}
