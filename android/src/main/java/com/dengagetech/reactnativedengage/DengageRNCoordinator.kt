package com.dengagetech.reactnativedengage

import android.content.Context
import android.util.Log
import com.dengage.sdk.DengageManager
import com.dengage.sdk.Dengage
import com.dengage.sdk.data.remote.api.DeviceConfigurationPreference
import com.dengage.sdk.data.remote.api.NotificationDisplayPriorityConfiguration
import com.facebook.react.ReactInstanceManager
import com.facebook.react.bridge.ReactContext

class DengageRNCoordinator private constructor() {
  private var reactInstanceManager: ReactInstanceManager? = null

  var initialized = false
    private set

  fun injectReactInstanceManager(reactInstanceManager: ReactInstanceManager) {
    if (this.reactInstanceManager != null) {
      Log.i(LOG_TAG, "DengageRNCoordinator already initialized.")
    }
    this.reactInstanceManager = reactInstanceManager

    this.reactInstanceManager!!.addReactInstanceEventListener(
      object : ReactInstanceManager.ReactInstanceEventListener {
        override fun onReactContextInitialized(context: ReactContext) {
          reactInstanceManager.removeReactInstanceEventListener(this)
          initialized = true
        }
      })
    this.reactInstanceManager!!.onNewIntent(null)
  }

  fun initDengage(
    firebaseIntegrationKey: String?,
    context: Context,
    deviceConfigurationPreference: DeviceConfigurationPreference,
    disableOpenWelUrl:Boolean?=false,
    logEnabled: Boolean
    ) {
    if (firebaseIntegrationKey == null) {
      throw Error("Firebase key can't be null");
    }

    Dengage.init(
      context = context,
      firebaseIntegrationKey = firebaseIntegrationKey,
      deviceConfigurationPreference = deviceConfigurationPreference,
      disableOpenWebUrl = disableOpenWelUrl,
      notificationDisplayPriorityConfiguration = NotificationDisplayPriorityConfiguration.SHOW_WITH_HIGH_PRIORITY
    )
    Dengage.setLogStatus(logEnabled)
  }

  companion object {
    private const val LOG_TAG: String = "DengageRNCoordinator"
    var sharedInstance = DengageRNCoordinator()
  }
}
