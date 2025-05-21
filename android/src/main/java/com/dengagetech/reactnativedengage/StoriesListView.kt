package com.dengagetech.reactnativedengage

import android.content.Context
import android.widget.FrameLayout
import com.dengage.sdk.ui.story.StoriesListView as DengageStoriesListView

class StoriesListView(context: Context) : FrameLayout(context) {

    var storiesListView = DengageStoriesListView(context)
    var hasShownStory = false

    init {
        addView(storiesListView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    }

}
