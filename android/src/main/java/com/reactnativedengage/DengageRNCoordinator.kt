package com.reactnativedengage

import android.content.Context
import com.dengage.sdk.DengageManager
import com.facebook.react.ReactInstanceManager
import com.facebook.react.bridge.ReactContext


class DengageRNCoordinator private constructor() {
  var reactInstanceManager: ReactInstanceManager? = null
  var dengageManager: DengageManager? = null

  var isSuccessfullyInitialized = false
    private set

  fun injectReactInstanceManager(reactInstanceManager: ReactInstanceManager) {
    if (this.reactInstanceManager != null) {
      // TODO: throw error. can only initialize once.

    }
    this.reactInstanceManager = reactInstanceManager

    this.reactInstanceManager!!.addReactInstanceEventListener(
      object : ReactInstanceManager.ReactInstanceEventListener {
        override fun onReactContextInitialized(context: ReactContext) {
          reactInstanceManager.removeReactInstanceEventListener(this)
          isSuccessfullyInitialized = true
        }
      })
  }

  fun setupDengage (logStatus: Boolean, firebaseKey: String?, huaweiKey: String?, context: Context) {
    if (firebaseKey == null && huaweiKey == null) {
      throw Error("Both firebase key and huawei key can't be null at the same time.");
    }

    when {
        huaweiKey == null -> {
          dengageManager = DengageManager.getInstance(context)
            .setLogStatus(logStatus)
            .setFirebaseIntegrationKey(firebaseKey)
            .init()
        }
        firebaseKey == null -> {
          dengageManager = DengageManager.getInstance(context)
            .setLogStatus(logStatus)
            .setHuaweiIntegrationKey(huaweiKey)
            .init()
        }
        else -> {
          dengageManager = DengageManager.getInstance(context)
            .setLogStatus(logStatus)
            .setHuaweiIntegrationKey(huaweiKey)
            .setFirebaseIntegrationKey(firebaseKey)
            .init()
        }
    }
  }

  companion object {
    var sharedInstance = DengageRNCoordinator()
  }
}
