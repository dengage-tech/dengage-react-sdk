package com.dengagetech.reactnativedengage

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

internal class StoryVisibilityChangedEvent(
    surfaceId: Int,
    viewId: Int,
    private val isHidden: Boolean
) : Event<StoryVisibilityChangedEvent>(surfaceId, viewId) {

    override fun getEventName(): String = EVENT_NAME

    override fun canCoalesce(): Boolean = false

    override fun getEventData(): WritableMap =
        Arguments.createMap().apply { putBoolean("isHidden", isHidden) }

    companion object {
        const val EVENT_NAME = "topStoryVisibilityChanged"
    }
}
