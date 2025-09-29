package com.reactnativedengage

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.dengage.sdk.Dengage
import com.dengage.sdk.ui.inappmessage.InAppInlineElement

/**
 * Created by mmpkl05 on 12/14/17.
 */
class InAppIineView(private val context: Context, val activity: Activity?) : InAppInlineElement(
    context
) {
    private var propertyId = ""
    private var screenName = ""
    var customParams = HashMap<String, String>()
    fun setPropertyId(propertyId: String) {
        this.propertyId = propertyId
        renderInAppInline()
    }

    fun setScreenName(screenName: String) {
        this.screenName = screenName
    }

    fun setCustomParams(customParams: HashMap<String, String>, isEmpty: Boolean) {
        if (!isEmpty) {
            this.customParams = customParams
        }
    }

    private fun renderInAppInline() {
      Handler(Looper.getMainLooper()).postDelayed({
        callInAppInline()

      }, 200)
    }

  private fun callInAppInline()
  {
    activity?.let {
      Dengage.showInlineInApp(propertyId=propertyId, screenName = screenName, customParams = customParams, inAppInlineElement = this,  activity = it)
    }
  }
}
