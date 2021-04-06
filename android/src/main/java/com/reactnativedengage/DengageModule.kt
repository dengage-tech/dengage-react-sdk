package com.reactnativedengage

import com.dengage.sdk.DengageEvent
import com.dengage.sdk.callback.DengageCallback
import com.dengage.sdk.models.DengageError
import com.dengage.sdk.models.InboxMessage
import com.facebook.react.bridge.*
import com.google.gson.Gson

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
    fun pageView (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).pageView(MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun addToCart (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).addToCart(MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun removeFromCart (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).removeFromCart(MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun viewCart (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).viewCart(MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun beginCheckout (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).beginCheckout(MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun placeOrder (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).order(MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun cancelOrder (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).cancelOrder(MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun addToWishList (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).addToWishList(MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun removeFromWishList (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).removeFromWishList(MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun search (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).search(MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun sendDeviceEvent (tableName: String, data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).sendDeviceEvent(tableName, MapUtil.toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun getInboxMessages (offset: Int, limit: Int, promise: Promise) {
      try {
        val callback = object : DengageCallback<List<InboxMessage>> {
          override fun onError(error: DengageError) {
            promise.reject(Error(error.errorMessage))
          }

          override fun onResult(result: List<InboxMessage>) {
            promise.resolve(Gson().toJson(result))
          }
        }
        DengageRNCoordinator.sharedInstance.dengageManager?.getInboxMessages(limit, offset, callback)
      } catch (ex: Exception) {
        promise.reject(ex)
      }
    }


    @ReactMethod
    fun deleteInboxMessage (id: String, promise: Promise) {
      try {
        DengageRNCoordinator.sharedInstance.dengageManager?.deleteInboxMessage(id)
        val map = Arguments.createMap()
        map.putBoolean("success", true)
        map.putString("id", id)
        promise.resolve(map)
      } catch (ex: Exception) {
        promise.reject(ex)
      }
    }

    @ReactMethod
    fun setInboxMessageAsClicked (id: String, promise: Promise) {
      try {
        DengageRNCoordinator.sharedInstance.dengageManager?.setInboxMessageAsClicked(id)
        val map = Arguments.createMap()
        map.putBoolean("success", true)
        map.putString("id", id)
        promise.resolve(map)
      } catch (ex: Exception) {
        promise.reject(ex)
      }
    }
}
