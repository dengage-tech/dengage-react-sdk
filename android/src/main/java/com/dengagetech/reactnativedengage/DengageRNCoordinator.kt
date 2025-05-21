package com.dengagetech.reactnativedengage

import android.app.Application
import android.content.Context
import android.util.Log
import com.facebook.react.ReactInstanceManager
import com.facebook.react.bridge.ReactContext
import com.dengage.sdk.Dengage
import com.dengage.sdk.data.remote.api.DeviceConfigurationPreference
import com.dengage.sdk.data.remote.api.NotificationDisplayPriorityConfiguration
import com.dengage.sdk.push.IDengageHmsManager
import com.dengage.sdk.util.DengageLifecycleTracker


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

  fun setupDengage(
    firebaseIntegrationKey: String?,
    huaweiIntegrationKey: String?,
    context: Context,
    dengageHmsManager: IDengageHmsManager? = null,
    deviceConfigurationPreference: DeviceConfigurationPreference,
    disableOpenWelUrl:Boolean?=false,
    logEnabled: Boolean = false,
    enableGeoFence: Boolean? = false
    ) {
    if (firebaseIntegrationKey == null) {
      throw Error("Firebase key can't be null");
    }

    if (context is Application) {
      context.registerActivityLifecycleCallbacks(DengageLifecycleTracker())
    }

    Dengage.init(
      context = context,
      firebaseIntegrationKey = firebaseIntegrationKey,
      huaweiIntegrationKey = huaweiIntegrationKey,
      dengageHmsManager = dengageHmsManager,
      deviceConfigurationPreference = deviceConfigurationPreference,
      disableOpenWebUrl = disableOpenWelUrl,
      notificationDisplayPriorityConfiguration = NotificationDisplayPriorityConfiguration.SHOW_WITH_HIGH_PRIORITY,
    )
    Dengage.setLogStatus(logEnabled)
    Dengage.inAppLinkConfiguration("www.chaitanyamunje.com")
    if (enableGeoFence == true) {
      try {
        val clazz = Class.forName("com.dengage.geofence.DengageGeofence")
        val instance = clazz.getField("INSTANCE").get(null)
        val method = clazz.getMethod("startGeofence")
        method.invoke(instance)
      } catch (e: ClassNotFoundException) {
        Log.w(LOG_TAG, "DengageGeofence library could not be found")
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }


  }

  companion object {
    private const val LOG_TAG: String = "DengageRNCoordinator"
    var sharedInstance = DengageRNCoordinator()
  }
}
