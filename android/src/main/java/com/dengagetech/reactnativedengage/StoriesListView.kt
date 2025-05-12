package com.dengagetech.reactnativedengage

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.dengage.sdk.ui.story.StoriesListView as DengageStoriesListView

class StoriesListView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

  var storiesListView = DengageStoriesListView(context)
  var hasShownStory = false
  init {
    addView(storiesListView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
  }

}
