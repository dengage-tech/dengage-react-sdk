package com.reactnativedengage

import com.dengage.sdk.DengageEvent
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.google.gson.Gson
import java.lang.Exception
import java.util.*

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
    fun setToken (token: String) {
      DengageRNCoordinator.sharedInstance.dengageManager?.subscription?.token = token
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

    @ReactMethod
    fun getContactKey(promise: Promise) {
      try {
        val contactKey = DengageRNCoordinator.sharedInstance.dengageManager?.subscription?.contactKey
        promise.resolve(contactKey)
      } catch (ex: Exception) {
        promise.reject(ex)
      }
    }

    @ReactMethod
    fun getUserPermission(promise: Promise) {
      try {
        val hasPermission = DengageRNCoordinator.sharedInstance.dengageManager?.subscription?.permission
        promise.resolve(hasPermission)
      } catch (ex: Exception) {
        promise.reject(ex)
      }
    }

    @ReactMethod
    fun getSubscription (promise: Promise) {
      try {
        promise.resolve(Gson().toJson(DengageRNCoordinator.sharedInstance.dengageManager?.subscription))
      } catch (ex: Exception) {
        promise.reject(ex)
      }
    }

    @ReactMethod
    fun pageView (data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).pageView(data)
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun addToCart (data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).addToCart(data)
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun removeFromCart (data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).removeFromCart(data)
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun viewCart (data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).viewCart(data)
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun beginCheckout (data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).beginCheckout(data)
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun placeOrder (data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).order(data)
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun cancelOrder (data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).cancelOrder(data)
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun addToWishList (data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).addToWishList(data)
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun removeFromWishList (data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).removeFromWishList(data)
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun search (data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).search(data)
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun sendDeviceEvent (tableName: String, data: Map<String, Any>) {
      try {
        DengageEvent.getInstance(reactApplicationContext).sendDeviceEvent(tableName, data)
      } catch (ex: Exception){
        print(ex)
      }
    }
}
