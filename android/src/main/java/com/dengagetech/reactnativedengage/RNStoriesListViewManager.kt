package com.dengagetech.reactnativedengage

import com.dengage.sdk.Dengage
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class RNStoriesListViewManager(
  private val reactContext: ReactApplicationContext
) : SimpleViewManager<StoriesListView>() {

  override fun getName() = "RCTStoriesListView"

  private var storyPropertyId: String? = null
  private var screenName: String? = null
  private var customParams: HashMap<String, String>? = null

  override fun createViewInstance(reactContext: ThemedReactContext): StoriesListView {
    return StoriesListView(reactContext)
  }

  @ReactProp(name = "storyPropertyId")
  fun setStoryPropertyId(view: StoriesListView, storyPropertyId: String) {
    this.storyPropertyId = storyPropertyId
    maybeShowStories(view)
  }

  @ReactProp(name = "screenName")
  fun setScreenName(view: StoriesListView, screenName: String) {
    this.screenName = screenName
    maybeShowStories(view)
  }

  @ReactProp(name = "customParams")
  fun setCustomParams(view: StoriesListView, customParams: ReadableMap?) {
    this.customParams = customParams.toHashMap()
    maybeShowStories(view)
  }

  private fun maybeShowStories(view: StoriesListView) {

    val currentStoryPropertyId = storyPropertyId
    val currentScreenName = screenName
    val currentCustomParams = customParams
    val currentActivity = reactContext.currentActivity

    if (!view.hasShownStory &&
      currentStoryPropertyId != null &&
      currentScreenName != null &&
      currentCustomParams != null &&
      currentActivity != null
    ) {
      view.hasShownStory = true
      Dengage.showStoriesList(
        storyPropertyId = currentStoryPropertyId,
        storiesListView = view.storiesListView,
        activity = currentActivity,
        customParams = currentCustomParams,
        screenName = currentScreenName
      )
    }
  }
}
