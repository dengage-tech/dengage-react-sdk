package com.dengagetech.reactnativedengage

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.dengage.sdk.Dengage
import java.util.stream.Collectors.toMap

class RNInAppInlineViewManager(
  private val reactContext: ReactApplicationContext
) : SimpleViewManager<InAppInlineView>() {

  override fun getName() = "RCTInAppInlineView"

  private var propertyId: String? = null
  private var screenName: String? = null
  private var customParams: HashMap<String, String>? = null

  override fun createViewInstance(reactContext: ThemedReactContext): InAppInlineView {
    return InAppInlineView(reactContext)
  }

  @ReactProp(name = "propertyId")
  fun setPropertyId(view: InAppInlineView, propertyId: String) {
    this.propertyId = propertyId
    maybeShowInlineInApp(view)
  }

  @ReactProp(name = "screenName")
  fun setScreenName(view: InAppInlineView, screenName: String) {
    this.screenName = screenName
    maybeShowInlineInApp(view)
  }

  @ReactProp(name = "customParams")
  fun setCustomParams(view: InAppInlineView, customParams: ReadableMap?) {
    this.customParams = customParams.toHashMap()
    maybeShowInlineInApp(view)
  }

  private fun maybeShowInlineInApp(view: InAppInlineView) {
    val currentPropertyId = propertyId
    val currentScreenName = screenName
    val currentCustomParams = customParams
    val currentActivity = reactContext.currentActivity

    if (!view.hasShownInline &&
      currentPropertyId != null &&
      currentScreenName != null &&
      currentCustomParams != null &&
      currentActivity != null
    ) {
      view.hasShownInline = true
      Dengage.showInlineInApp(
        screenName = currentScreenName,
        inAppInlineElement = view.inlineElement,
        propertyId = currentPropertyId,
        activity = currentActivity,
        customParams = currentCustomParams
      )
    }
  }
}
