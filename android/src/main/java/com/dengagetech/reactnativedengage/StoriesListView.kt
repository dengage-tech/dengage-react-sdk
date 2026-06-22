package com.dengagetech.reactnativedengage

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.dengage.sdk.Dengage
import com.dengage.sdk.ui.story.StoriesListView as DengageStoriesListView
import com.facebook.react.bridge.GuardedRunnable
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.UIManagerModule

class StoriesListView(context: Context) : FrameLayout(context) {
    val dengageStoriesListView = DengageStoriesListView(context)

    var reactStoryPropertyId: String? = null
    var reactScreenName: String? = null
    var reactCustomParams: HashMap<String, String>? = null
    var reactHideIfNotFound: Boolean = true

    private var lastAppliedConfigurationSignature: String? = null
    private var measuredContentHeight: Int = 0
    private var contentLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    private val applyConfigurationRunnable = Runnable { applyStoryConfigurationIfReady() }

    private val visibilityPollHandler = Handler(Looper.getMainLooper())
    private var lastSentIsHidden: Boolean? = null
    private var hiddenSinceElapsed: Long? = null

    private val visibilityPollRunnable = object : Runnable {
        override fun run() {
            if (!isAttachedToWindow) {
                return
            }
            val rawHidden = isStoryEffectivelyHidden
            val now = SystemClock.elapsedRealtime()
            val debouncedHidden = if (rawHidden) {
                if (hiddenSinceElapsed == null) {
                    hiddenSinceElapsed = now
                }
                (now - (hiddenSinceElapsed ?: now)) >= HIDDEN_DEBOUNCE_MS
            } else {
                hiddenSinceElapsed = null
                false
            }
            if (lastSentIsHidden == null || lastSentIsHidden != debouncedHidden) {
                lastSentIsHidden = debouncedHidden
                log(
                    "visibilityPoll: rawHidden=$rawHidden debouncedHidden=$debouncedHidden " +
                        "childVisibility=${visibilityName(dengageStoriesListView.visibility)} " +
                        "adapterCount=${adapterItemCount()}"
                )
                if (debouncedHidden) {
                    applyCollapsedNativeLayout(true)
                } else {
                    updateVisibleHeightIfNeeded("visibilityPoll")
                }
                dispatchStoryVisibility(debouncedHidden)
            }
            visibilityPollHandler.postDelayed(this, POLL_MS)
        }
    }

    private val isStoryEffectivelyHidden: Boolean
        get() = dengageStoriesListView.visibility != View.VISIBLE

    private val performShowRunnable = Runnable {
        log("performShowRunnable: start")
        val themedContext = context as? ThemedReactContext
        if (themedContext == null) {
            log("performShowRunnable: abort — context is not ThemedReactContext")
            return@Runnable
        }
        val activity = themedContext.currentActivity
            ?: themedContext.reactApplicationContext.currentActivity
        if (activity == null) {
            log("performShowRunnable: abort — activity is null")
            return@Runnable
        }
        val expected = lastAppliedConfigurationSignature
        if (expected == null) {
            log("performShowRunnable: abort — no applied configuration signature")
            return@Runnable
        }
        if (computeConfigurationSignature() != expected) {
            log("performShowRunnable: abort — configuration changed since schedule")
            return@Runnable
        }
        log(
            "performShowRunnable: calling showStoriesList " +
                "propertyId=$reactStoryPropertyId screenName=$reactScreenName " +
                "hideIfNotFound=$reactHideIfNotFound customParams=$reactCustomParams " +
                "activity=${activity.javaClass.simpleName}"
        )
        Dengage.showStoriesList(
            storyPropertyId = reactStoryPropertyId!!,
            storiesListView = dengageStoriesListView,
            activity = activity,
            customParams = reactCustomParams?.takeIf { it.isNotEmpty() },
            screenName = reactScreenName!!.ifEmpty { null },
            hideIfNotFound = reactHideIfNotFound
        )
        log(
            "performShowRunnable: showStoriesList returned " +
                "childVisibility=${visibilityName(dengageStoriesListView.visibility)} " +
                "adapterCount=${adapterItemCount()}"
        )
        registerContentLayoutListener()
        scheduleHeightUpdates("performShowRunnable")
        dengageStoriesListView.post {
            updateVisibleHeightIfNeeded("post-show")
            emitVisibilityFromCurrentState("post-show")
        }
        postDelayed({ emitVisibilityFromCurrentState("post-show+200ms") }, 200)
    }

    init {
        addView(
            dengageStoriesListView,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        )
        log("init: embedded StoriesListView added")
    }

    fun cleanup() {
        unregisterContentLayoutListener()
        visibilityPollHandler.removeCallbacks(visibilityPollRunnable)
        removeCallbacks(applyConfigurationRunnable)
        removeCallbacks(performShowRunnable)
    }

    fun scheduleApplyStoryConfiguration() {
        log(
            "scheduleApplyStoryConfiguration: propertyId=$reactStoryPropertyId " +
                "screenName=$reactScreenName customParams=$reactCustomParams " +
                "attached=$isAttachedToWindow"
        )
        removeCallbacks(applyConfigurationRunnable)
        removeCallbacks(performShowRunnable)
        post(applyConfigurationRunnable)
    }

    fun refreshStory() {
        log("refreshStory")
        lastAppliedConfigurationSignature = null
        scheduleApplyStoryConfiguration()
    }

    private fun computeConfigurationSignature(): String? {
        val propertyId = reactStoryPropertyId ?: return null
        val screenName = reactScreenName ?: return null
        val params = reactCustomParams ?: return null
        val paramsPart =
            params.entries.sortedBy { it.key }.joinToString(separator = "\u0001") { "${it.key}=${it.value}" }
        return "$propertyId\u0000$screenName\u0000$paramsPart\u0000$reactHideIfNotFound"
    }

    private fun resetStoryContent() {
        log("resetStoryContent")
        lastSentIsHidden = null
        hiddenSinceElapsed = null
        measuredContentHeight = 0
        dengageStoriesListView.visibility = View.VISIBLE
        DengageStoriesListView.recyclerView?.adapter = null
        requestLayout()
    }

    private fun applyStoryConfigurationIfReady() {
        if (!isAttachedToWindow) {
            log("applyStoryConfigurationIfReady: skip — not attached to window")
            return
        }
        val signature = computeConfigurationSignature()
        if (signature == null) {
            log(
                "applyStoryConfigurationIfReady: skip — incomplete props " +
                    "propertyId=$reactStoryPropertyId screenName=$reactScreenName " +
                    "customParams=$reactCustomParams"
            )
            return
        }
        if (signature == lastAppliedConfigurationSignature) {
            log("applyStoryConfigurationIfReady: skip — signature unchanged")
            return
        }
        val reloading = lastAppliedConfigurationSignature != null
        log("applyStoryConfigurationIfReady: applying reloading=$reloading signature=$signature")
        if (reloading) {
            resetStoryContent()
        }
        lastAppliedConfigurationSignature = signature
        if (reloading) {
            post(performShowRunnable)
        } else {
            performShowRunnable.run()
        }
        post { emitVisibilityFromCurrentState("apply-config") }
    }

    private fun applyCollapsedNativeLayout(hidden: Boolean) {
        if (hidden) {
            measuredContentHeight = 0
            log("applyCollapsedNativeLayout: hidden=true height=0")
            notifyReactNativeOfSizeChange(width.coerceAtLeast(MeasureSpec.getSize(lastWidthMeasureSpec)), 0)
        } else {
            log("applyCollapsedNativeLayout: hidden=false — will remeasure on next layout pass")
        }
        requestLayout()
    }

    private fun updateVisibleHeightIfNeeded(source: String) {
        if (isStoryEffectivelyHidden) {
            log("updateVisibleHeightIfNeeded[$source]: hidden — collapsing")
            measuredContentHeight = 0
            requestLayout()
            return
        }

        val measureWidth = when {
            width > 0 -> width
            MeasureSpec.getSize(lastWidthMeasureSpec) > 0 -> MeasureSpec.getSize(lastWidthMeasureSpec)
            else -> resources.displayMetrics.widthPixels
        }
        val widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        dengageStoriesListView.measure(widthSpec, heightSpec)
        var measuredHeight = dengageStoriesListView.measuredHeight

        val recyclerView = DengageStoriesListView.recyclerView
        if (measuredHeight <= 0 && recyclerView != null && recyclerView.height > 0) {
            measuredHeight = recyclerView.height + dengageStoriesListView.paddingTop + dengageStoriesListView.paddingBottom
        }

        log(
            "updateVisibleHeightIfNeeded[$source]: measureWidth=$measureWidth " +
                "measuredHeight=$measuredHeight storedHeight=$measuredContentHeight " +
                "wrapperHeight=$height childHeight=${dengageStoriesListView.height} " +
                "recyclerHeight=${recyclerView?.height} adapterCount=${adapterItemCount()}"
        )

        if (measuredHeight <= 0) {
            return
        }

        val targetHeight = measuredHeight.coerceAtLeast(1)
        if (measuredContentHeight != targetHeight) {
            log("updateVisibleHeightIfNeeded[$source]: storing content height=$targetHeight")
            measuredContentHeight = targetHeight
            notifyReactNativeOfSizeChange(measureWidth, targetHeight)
            requestLayout()
        }
    }

    private fun notifyReactNativeOfSizeChange(width: Int, height: Int) {
        val themedContext = context as? ThemedReactContext ?: return
        if (id == NO_ID) {
            log("notifyReactNativeOfSizeChange: skip — no react tag")
            return
        }
        val layoutWidth = width.coerceAtLeast(1)
        log("notifyReactNativeOfSizeChange: ${layoutWidth}x$height reactTag=$id")
        themedContext.runOnNativeModulesQueueThread(object : GuardedRunnable(themedContext) {
            override fun runGuarded() {
                themedContext.reactApplicationContext
                    .getNativeModule(UIManagerModule::class.java)
                    ?.updateNodeSize(id, layoutWidth, height)
            }
        })
    }

    private fun scheduleHeightUpdates(source: String) {
        val delays = longArrayOf(0L, 50L, 200L, 500L)
        for (delay in delays) {
            postDelayed({ updateVisibleHeightIfNeeded("$source+${delay}ms") }, delay)
        }
    }

    private fun registerContentLayoutListener() {
        unregisterContentLayoutListener()
        val recyclerView = DengageStoriesListView.recyclerView ?: return
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            log(
                "contentLayoutListener: recycler ${recyclerView.width}x${recyclerView.height} " +
                    "adapterCount=${adapterItemCount()}"
            )
            updateVisibleHeightIfNeeded("content-layout")
        }
        contentLayoutListener = listener
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    private fun unregisterContentLayoutListener() {
        val recyclerView = DengageStoriesListView.recyclerView
        val listener = contentLayoutListener
        if (recyclerView != null && listener != null && recyclerView.viewTreeObserver.isAlive) {
            recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
        contentLayoutListener = null
    }

    private fun emitVisibilityFromCurrentState(source: String) {
        val hidden = isStoryEffectivelyHidden
        log(
            "emitVisibilityFromCurrentState[$source]: hidden=$hidden lastSent=$lastSentIsHidden " +
                "childVisibility=${visibilityName(dengageStoriesListView.visibility)} " +
                "wrapperHeight=$height adapterCount=${adapterItemCount()}"
        )
        if (lastSentIsHidden == null || lastSentIsHidden != hidden) {
            lastSentIsHidden = hidden
            if (hidden) {
                applyCollapsedNativeLayout(true)
            } else {
                updateVisibleHeightIfNeeded("emit-$source")
            }
            dispatchStoryVisibility(hidden)
        }
    }

    private fun dispatchStoryVisibility(isHidden: Boolean) {
        val themedContext = context as? ThemedReactContext
        if (themedContext == null) {
            log("dispatchStoryVisibility: abort — no ThemedReactContext")
            return
        }
        val dispatcher = UIManagerHelper.getEventDispatcherForReactTag(themedContext, id)
        if (dispatcher == null) {
            log("dispatchStoryVisibility: abort — no event dispatcher for reactTag=$id")
            return
        }
        log("dispatchStoryVisibility: isHidden=$isHidden reactTag=$id")
        dispatcher.dispatchEvent(
            StoryVisibilityChangedEvent(
                UIManagerHelper.getSurfaceId(this),
                id,
                isHidden
            )
        )
    }

    private fun adapterItemCount(): Int =
        DengageStoriesListView.recyclerView?.adapter?.itemCount ?: -1

    private fun visibilityName(visibility: Int): String = when (visibility) {
        View.VISIBLE -> "VISIBLE"
        View.GONE -> "GONE"
        View.INVISIBLE -> "INVISIBLE"
        else -> visibility.toString()
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }

    private fun startVisibilityPolling() {
        visibilityPollHandler.removeCallbacks(visibilityPollRunnable)
        lastSentIsHidden = null
        hiddenSinceElapsed = null
        visibilityPollHandler.post(visibilityPollRunnable)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        log("onAttachedToWindow")
        scheduleApplyStoryConfiguration()
        startVisibilityPolling()
    }

    override fun onDetachedFromWindow() {
        log("onDetachedFromWindow")
        cleanup()
        super.onDetachedFromWindow()
    }

    private var lastWidthMeasureSpec: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        lastWidthMeasureSpec = widthMeasureSpec
        val width = MeasureSpec.getSize(widthMeasureSpec).coerceAtLeast(0)
        if (isStoryEffectivelyHidden) {
            log("onMeasure: hidden width=$width height=0")
            setMeasuredDimension(width, 0)
            return
        }
        if (measuredContentHeight > 0) {
            log("onMeasure: using stored height width=$width height=$measuredContentHeight")
            setMeasuredDimension(width, measuredContentHeight)
            return
        }
        if (width > 0) {
            val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
            val childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            dengageStoriesListView.measure(widthSpec, childHeightSpec)
            val measuredHeight = dengageStoriesListView.measuredHeight
            if (measuredHeight > 0) {
                measuredContentHeight = measuredHeight
                log("onMeasure: measured child width=$width height=$measuredHeight")
                setMeasuredDimension(width, measuredHeight)
                return
            }
        }
        log("onMeasure: fallback super width=$width")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        val parentHeight = bottom - top
        val layoutHeight = when {
            isStoryEffectivelyHidden -> 0
            measuredContentHeight > 0 -> measuredContentHeight
            else -> parentHeight
        }
        if (width > 0 && measuredContentHeight > 0 && parentHeight < measuredContentHeight) {
            notifyReactNativeOfSizeChange(width, measuredContentHeight)
        }
        dengageStoriesListView.layout(0, 0, width, layoutHeight)
        log(
            "onLayout: changed=$changed wrapper=${width}x${parentHeight} layoutHeight=$layoutHeight " +
                "storedHeight=$measuredContentHeight " +
                "child=${dengageStoriesListView.width}x${dengageStoriesListView.height} " +
                "childVisibility=${visibilityName(dengageStoriesListView.visibility)} " +
                "adapterCount=${adapterItemCount()}"
        )
    }

    companion object {
        private const val TAG = "DengageRN/StoriesList"
        private const val POLL_MS = 50L
        private const val HIDDEN_DEBOUNCE_MS = 0L
    }
}
