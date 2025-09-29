package com.reactnativedengage

import android.app.Activity
import android.util.Log
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

/**
 * Created by mmpkl05 on 12/14/17.
 */
class InAppInlineViewManager(private val activity: Activity?) : SimpleViewManager<InAppIineView>() {

    override fun getName(): String {
        return REACT_CLASS
    }

    public override fun createViewInstance(context: ThemedReactContext): InAppIineView {
        Log.i("Create View Instance", "ANDROID_SAMPLE_UI")
        return InAppIineView(
            context, activity ?: context.currentActivity
        )
    }

  @ReactProp(name = "propertyId")
    fun setPropertyId(view: InAppIineView, propertyId: String?) {
        Log.i("Set Message", "ANDROID_SAMPLE_UI $propertyId")
      if (propertyId != null) {
        view.setPropertyId(propertyId)
      }
    }

    @ReactProp(name = "screenName")
    fun setScreenName(view: InAppIineView, screenName: String?) {
        Log.i("Set Message", "ANDROID_SAMPLE_UI $screenName")
      if (screenName != null) {
        view.setScreenName(screenName)
      }
    }
    @ReactProp(name = "customParams")
    fun setCustomParams(view: InAppIineView, data: ReadableMap?) {
        Log.i("Set Message", "ANDROID_SAMPLE_UI $data")

        view.setCustomParams((MapUtil.toMap(data) as HashMap<String, String>),(MapUtil.toMap(data) as HashMap<String, String>).isEmpty())
    }

    companion object {
        const val REACT_CLASS = "InAppInlineView"
    }
}
