package com.dengagetech.reactnativedengage

import android.content.Context
import android.widget.FrameLayout
import android.webkit.WebView
import android.webkit.WebViewClient
import android.os.Handler
import android.os.Looper
import com.dengage.sdk.ui.inappmessage.InAppInlineElement


class InAppInlineView(context: Context) : FrameLayout(context) {
  val inlineElement = InAppInlineElement(context)
  var hasShownInline = false

  init {
    addView(inlineElement, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

    inlineElement.webViewClient = object : WebViewClient() {
      override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        view?.evaluateJavascript(
          "(function() { return document.body.scrollHeight; })();"
        ) { value ->
          val height = value?.toFloatOrNull() ?: return@evaluateJavascript
          Handler(Looper.getMainLooper()).post {
            val params = inlineElement.layoutParams
            params.height = height.toInt()
            inlineElement.layoutParams = params
            this@InAppInlineView.requestLayout()
          }
        }
      }
    }
  }
}
