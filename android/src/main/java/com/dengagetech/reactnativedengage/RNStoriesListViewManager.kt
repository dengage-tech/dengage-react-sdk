package com.dengagetech.reactnativedengage

import android.util.Log
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class RNStoriesListViewManager : SimpleViewManager<StoriesListView>() {

    override fun getName() = "RCTStoriesListView"

    override fun createViewInstance(reactContext: ThemedReactContext): StoriesListView {
        return StoriesListView(reactContext)
    }

    override fun onDropViewInstance(view: StoriesListView) {
        view.cleanup()
        super.onDropViewInstance(view)
    }

    override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any>? {
        val base = super.getExportedCustomDirectEventTypeConstants() ?: HashMap()
        base[StoryVisibilityChangedEvent.EVENT_NAME] =
            MapBuilder.of("registrationName", "onStoryVisibilityChanged")
        return base
    }

    override fun getCommandsMap(): MutableMap<String, Int> =
        MapBuilder.of(COMMAND_REFRESH, COMMAND_REFRESH_ID)

    @Deprecated("Use receiveCommand with String commandId")
    override fun receiveCommand(root: StoriesListView, commandId: Int, args: ReadableArray?) {
        if (commandId == COMMAND_REFRESH_ID) {
            root.refreshStory()
        }
    }

    override fun receiveCommand(root: StoriesListView, commandId: String, args: ReadableArray?) {
        if (commandId == COMMAND_REFRESH) {
            root.refreshStory()
        }
    }

    @ReactProp(name = "storyPropertyId")
    fun setStoryPropertyId(view: StoriesListView, storyPropertyId: String) {
        Log.d(TAG, "setStoryPropertyId: $storyPropertyId")
        view.reactStoryPropertyId = storyPropertyId
        view.scheduleApplyStoryConfiguration()
    }

    @ReactProp(name = "screenName")
    fun setScreenName(view: StoriesListView, screenName: String) {
        Log.d(TAG, "setScreenName: $screenName")
        view.reactScreenName = screenName
        view.scheduleApplyStoryConfiguration()
    }

    @ReactProp(name = "customParams")
    fun setCustomParams(view: StoriesListView, customParams: ReadableMap?) {
        Log.d(TAG, "setCustomParams: $customParams")
        view.reactCustomParams = customParams.toHashMap() ?: hashMapOf()
        view.scheduleApplyStoryConfiguration()
    }

    @ReactProp(name = "hideIfNotFound", defaultBoolean = true)
    fun setHideIfNotFound(view: StoriesListView, hideIfNotFound: Boolean) {
        Log.d(TAG, "setHideIfNotFound: $hideIfNotFound")
        view.reactHideIfNotFound = hideIfNotFound
        view.scheduleApplyStoryConfiguration()
    }

    companion object {
        private const val TAG = "DengageRN/StoriesList"
        const val COMMAND_REFRESH = "refresh"
        private const val COMMAND_REFRESH_ID = 1
    }
}
