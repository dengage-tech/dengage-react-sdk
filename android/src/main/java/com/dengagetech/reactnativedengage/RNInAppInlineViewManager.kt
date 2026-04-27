package com.dengagetech.reactnativedengage

import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class RNInAppInlineViewManager : SimpleViewManager<InAppInlineView>() {

    override fun getName() = "RCTInAppInlineView"

    override fun createViewInstance(reactContext: ThemedReactContext): InAppInlineView {
        return InAppInlineView(reactContext)
    }

    @ReactProp(name = "propertyId")
    fun setPropertyId(view: InAppInlineView, propertyId: String) {
        view.reactPropertyId = propertyId
        view.scheduleApplyInlineConfiguration()
    }

    @ReactProp(name = "screenName")
    fun setScreenName(view: InAppInlineView, screenName: String) {
        view.reactScreenName = screenName
        view.scheduleApplyInlineConfiguration()
    }

    @ReactProp(name = "customParams")
    fun setCustomParams(view: InAppInlineView, customParams: ReadableMap?) {
        view.reactCustomParams = customParams.toHashMap()
        view.scheduleApplyInlineConfiguration()
    }
}
