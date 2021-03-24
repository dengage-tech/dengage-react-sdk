package com.reactnativedengage

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import java.lang.Exception

class DengageModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "DengageRN"
    }

    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    fun multiply(a: Int, b: Int, promise: Promise) {
      promise.resolve(a * b)
    }

    @ReactMethod
    fun setHuaweiIntegrationKey (key: String) {
      DengageRNCoordinator.sharedInstance.dengageManager?.setHuaweiIntegrationKey(key)
    }

    @ReactMethod
    fun setFirebaseIntegrationKey (key: String) {
      DengageRNCoordinator.sharedInstance.dengageManager?.setFirebaseIntegrationKey(key)
    }

    @ReactMethod
    fun setContactKey (contactKey: String) {
      DengageRNCoordinator.sharedInstance.dengageManager?.setContactKey(contactKey)
    }

    @ReactMethod
    fun setLogStatus (logStatus: Boolean) {
      DengageRNCoordinator.sharedInstance.dengageManager?.setLogStatus(logStatus)
    }

    @ReactMethod
    fun setPermission (hasPermission: Boolean) {
      DengageRNCoordinator.sharedInstance.dengageManager?.setPermission(hasPermission)
    }

    @ReactMethod
    fun getToken (promise: Promise) {
      try {
        val token = DengageRNCoordinator.sharedInstance.dengageManager?.subscription?.token
        if (token !== null) {
          promise.resolve(token)
          return
        }
        throw Exception("unable to get token.");
      } catch (ex: Exception) {
        promise.reject(ex)
      }
    }


}
