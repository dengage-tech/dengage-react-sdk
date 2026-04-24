package com.dengagetech.reactnativedengage

import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class RNInAppInlineViewManager(
    @Suppress("UNUSED_PARAMETER") reactContext: ReactApplicationContext
) : SimpleViewManager<InAppInlineView>() {

    override fun getName() = "RCTInAppInlineView"

    override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any>? =
        com.facebook.react.common.MapBuilder.of(
            "topVisibilityChanged",
            com.facebook.react.common.MapBuilder.of("registrationName", "onVisibilityChanged")
        )

    override fun createViewInstance(reactContext: ThemedReactContext): InAppInlineView {
        return InAppInlineView(reactContext)
    }

    override fun onDropViewInstance(view: InAppInlineView) {
        view.dispose()
        super.onDropViewInstance(view)
    }

    @ReactProp(name = "propertyId")
    fun setPropertyId(view: InAppInlineView, propertyId: String) {
        view.propertyId = propertyId
        view.maybeShowInlineInApp()
    }

    @ReactProp(name = "screenName")
    fun setScreenName(view: InAppInlineView, screenName: String) {
        view.screenName = screenName
        view.maybeShowInlineInApp()
    }

    @ReactProp(name = "customParams")
    fun setCustomParams(view: InAppInlineView, customParams: ReadableMap?) {
        view.customParams = customParams.toHashMap() ?: HashMap()
        view.maybeShowInlineInApp()
    }

    @ReactProp(name = "hideIfNotFound")
    fun setHideIfNotFound(view: InAppInlineView, hideIfNotFound: Boolean) {
        view.hideIfNotFound = hideIfNotFound
        view.maybeShowInlineInApp()
    }
}
