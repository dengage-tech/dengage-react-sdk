package com.reactnativedengage

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dengage.sdk.DengageEvent
import com.dengage.sdk.NotificationReceiver
import com.dengage.sdk.callback.DengageCallback
import com.dengage.sdk.models.DengageError
import com.dengage.sdk.models.InboxMessage
import com.dengage.sdk.models.Message
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.google.gson.Gson
import com.reactnativedengage.MapUtil.convertJsonToMap
import com.reactnativedengage.MapUtil.toMap
import org.json.JSONObject


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
        DengageEvent.getInstance(reactApplicationContext).pageView(toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun addToCart (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).addToCart(toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun removeFromCart (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).removeFromCart(toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun viewCart (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).viewCart(toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun beginCheckout (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).beginCheckout(toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun placeOrder (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).order(toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun cancelOrder (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).cancelOrder(toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun addToWishList (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).addToWishList(toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun removeFromWishList (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).removeFromWishList(toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun search (data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).search(toMap(data))
      } catch (ex: Exception){
        print(ex)
      }
    }

    @ReactMethod
    fun sendDeviceEvent (tableName: String, data: ReadableMap) {
      try {
        DengageEvent.getInstance(reactApplicationContext).sendDeviceEvent(tableName, toMap(data))
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

    @ReactMethod
    fun setNavigation () {
      DengageRNCoordinator.sharedInstance.dengageManager?.setNavigation(currentActivity as AppCompatActivity)
    }

    @ReactMethod
    fun setNavigationWithName (screenName: String) {
      DengageRNCoordinator.sharedInstance.dengageManager?.setNavigation(currentActivity as AppCompatActivity, screenName)
    }

    @ReactMethod
    fun registerNotificationListeners () {

        Log.d("den/react-native", "RegisteringNotificationListeners.")

        val filter = IntentFilter()
        filter.addAction("com.dengage.push.intent.RECEIVE")
        filter.addAction("com.dengage.push.intent.OPEN")
        val notifReceiver = NotifReciever(reactApplicationContext)
        reactApplicationContext.currentActivity?.registerReceiver(notifReceiver, filter)
    }

    class NotifReciever(reactAppContext: ReactApplicationContext) : NotificationReceiver() {
        var reactApplicationContext: ReactApplicationContext? = reactAppContext

        override fun onReceive(context: Context?, intent: Intent) {
            val intentAction = intent.action
            if (intentAction != null) {
                when (intentAction.hashCode()) {
                    -825236177 -> {
                        if (intentAction == "com.dengage.push.intent.RECEIVE") {
                            Log.d("den/react-native", "received new push.")
                            val message: Message = intent.getExtras()?.let { Message(it) }!!
                            sendEvent("onNotificationReceived", convertJsonToMap(JSONObject(Gson().toJson(message)))!!, reactApplicationContext)
                        }
                    }
                    -520704162 -> {
                        // intentAction == "com.dengage.push.intent.RECEIVE"
                        Log.d("den/react-native", "push is clicked.")
                        val message: Message = intent.getExtras()?.let { Message(it) }!!
                        sendEvent("onNotificationClicked", convertJsonToMap(JSONObject(Gson().toJson(message)))!!, reactApplicationContext)
                    }
                }
            }
        }
    }

    companion object {
      var reactContext: ReactApplicationContext? = null

      fun sendEvent (eventName: String, data: WritableMap, reactAppContext: ReactApplicationContext? = null) {
          Log.d("sendingEvent", eventName)
        if (reactContext != null) {
          reactContext
            ?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            ?.emit(eventName, data)
        } else if (reactAppContext != null) {
            reactAppContext
                    ?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    ?.emit(eventName, data)
        } else {
            Log.d("no reactContext.", "unable to have a react context.")
        }
      }
    }
}
