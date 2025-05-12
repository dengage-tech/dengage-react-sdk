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
    addView(inlineElement, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

    inlineElement.webViewClient = object : WebViewClient() {
      override fun onPageFinished(view: WebView?, url: String?) {
        // Evaluate JS to get content height and update layout
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
}
