package com.dengagetech.reactnativedengage

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.dengage.sdk.ui.story.StoriesListView
import com.dengage.sdk.Dengage
import com.facebook.react.bridge.ReadableMap

class RNStoriesListViewManager(
  val context: ReactApplicationContext
) : SimpleViewManager<StoriesListView>() {

  companion object {
    const val VIEW_NAME = "RCTStoriesListView"
    const val COMMAND_GET_STORIES = 1
  }

  private var storyPropertyId: String? = null
  private var screenName: String? = null
  private var customParams: ReadableMap? = null

  override fun getName() = VIEW_NAME

  override fun createViewInstance(reactContext: ThemedReactContext): StoriesListView {
    val storiesListView = StoriesListView(reactContext)
    return storiesListView
  }

  override fun getCommandsMap(): Map<String, Int> {
    return MapBuilder.of("getStories", COMMAND_GET_STORIES)
  }

  override fun receiveCommand(
    root: StoriesListView,
    commandId: String,
    args: ReadableArray?
  ) {
    super.receiveCommand(root, commandId, args)
    val viewId = args?.getInt(0) ?: return
    val commandIdInt = commandId.toIntOrNull() ?: return

    if (commandIdInt == COMMAND_GET_STORIES) {
      getStories(root, viewId)
    }
  }

  @ReactProp(name = "storyPropertyId")
  fun setStoryPropertyId(view: StoriesListView, storyPropertyId: String?) {
    this.storyPropertyId = storyPropertyId
  }

  @ReactProp(name = "screenName")
  fun setScreenName(view: StoriesListView, screenName: String?) {
    this.screenName = screenName
  }

  @ReactProp(name = "customParams")
  fun setCustomParams(view: StoriesListView, data: ReadableMap?) {
    Log.i("Set Message", "ANDROID_SAMPLE_UI $data")
    this.customParams = data
  }

  private fun getStories(storiesListView: StoriesListView, viewId: Int) {

    Dengage.showStoriesList(screenName = screenName?.ifEmpty { null },
      storiesListView = storiesListView,
      storyPropertyId = storyPropertyId ?: "",
      activity = context.currentActivity ?: return,
      customParams = null
    )

  }

  /*
  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any> {
    return MapBuilder.builder<String, Any>()
      .put(
        "onItemClicked",
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onItemClicked")
        )
      )
      .build()
  }
   */

  private fun sendData(data: WritableMap, viewId: Int) {
    context.getJSModule(RCTEventEmitter::class.java)
      .receiveEvent(viewId, "onItemClicked", data)
  }
}



