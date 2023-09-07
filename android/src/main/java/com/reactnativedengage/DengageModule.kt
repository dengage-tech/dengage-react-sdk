package com.reactnativedengage

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.dengage.sdk.Dengage
import com.dengage.sdk.DengageManager
import com.dengage.sdk.callback.DengageCallback
import com.dengage.sdk.callback.DengageError
import com.dengage.sdk.domain.inboxmessage.model.InboxMessage
import com.dengage.sdk.domain.push.model.Message
import com.dengage.sdk.inapp.InAppBroadcastReceiver
import com.dengage.sdk.push.NotificationReceiver

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.google.gson.Gson
import com.reactnativedengage.MapUtil.convertJsonToMap
import com.reactnativedengage.MapUtil.convertMapToJson
import com.reactnativedengage.MapUtil.toMap
import org.json.JSONObject


class DengageModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

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
  fun setFirebaseIntegrationKey(key: String) {
    DengageRNCoordinator.sharedInstance.dengageManager?.setFirebaseIntegrationKey(key)
  }

  @ReactMethod
  fun setContactKey(contactKey: String) {
    DengageRNCoordinator.sharedInstance.dengageManager?.setContactKey(contactKey)
  }

  @ReactMethod
  fun setLogStatus(logStatus: Boolean) {
    DengageRNCoordinator.sharedInstance.dengageManager?.setLogStatus(logStatus)
  }

  @ReactMethod
  fun setPermission(hasPermission: Boolean) {
    DengageRNCoordinator.sharedInstance.dengageManager?.setPermission(hasPermission)
  }

  @ReactMethod
  fun setToken(token: String) {
    DengageRNCoordinator.sharedInstance.dengageManager?.onNewToken(token)
  }


  @ReactMethod
  fun getToken(promise: Promise) {
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
      val hasPermission =
        DengageRNCoordinator.sharedInstance.dengageManager?.subscription?.permission
      promise.resolve(hasPermission)
    } catch (ex: Exception) {
      promise.reject(ex)
    }
  }

  @ReactMethod
  fun getSubscription(promise: Promise) {
    try {
      promise.resolve(Gson().toJson(DengageRNCoordinator.sharedInstance.dengageManager?.subscription))
    } catch (ex: Exception) {
      promise.reject(ex)
    }
  }

  @ReactMethod
  fun pageView(data: ReadableMap) {
    try {
       Dengage.pageView(toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun addToCart(data: ReadableMap) {
    try {
      Dengage.addToCart(toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun removeFromCart(data: ReadableMap) {
    try {
      Dengage.removeFromCart(toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun viewCart(data: ReadableMap) {
    try {
      Dengage.viewCart(toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun beginCheckout(data: ReadableMap) {
    try {
      Dengage.beginCheckout(toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun placeOrder(data: ReadableMap) {
    try {
       Dengage.order(toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun cancelOrder(data: ReadableMap) {
    try {
       Dengage.cancelOrder(toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun addToWishList(data: ReadableMap) {
    try {
      Dengage.addToWishList(toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun removeFromWishList(data: ReadableMap) {
    try {
      Dengage.removeFromWishList(toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun search(data: ReadableMap) {
    try {
      Dengage.search(toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun sendDeviceEvent(tableName: String, data: ReadableMap) {
    try {
      Dengage.sendDeviceEvent(tableName,toMap(data) as HashMap<String, Any>)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun getInboxMessages(offset: Int, limit: Int, promise: Promise) {
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
  fun deleteInboxMessage(id: String, promise: Promise) {
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
  fun setInboxMessageAsClicked(id: String, promise: Promise) {
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
  fun setNavigation() {
    DengageRNCoordinator.sharedInstance.dengageManager?.setNavigation(currentActivity as AppCompatActivity)
  }

  @ReactMethod
  fun setNavigationWithName(screenName: String) {
    DengageRNCoordinator.sharedInstance.dengageManager?.setNavigation(
      currentActivity as AppCompatActivity,
      screenName
    )
  }

  @ReactMethod
  fun registerNotificationListeners() {

    Log.d("den/react-native", "RegisteringNotificationListeners.")

    val filter = IntentFilter()
    filter.addAction("com.dengage.push.intent.RECEIVE")
    filter.addAction("com.dengage.push.intent.OPEN")
    val notifReceiver = NotifReciever(reactApplicationContext)
    reactApplicationContext.currentActivity?.registerReceiver(notifReceiver, filter)

  }

  @ReactMethod
  fun registerInAppListener() {
    val inappFilter = IntentFilter()
    inappFilter.addAction("com.dengage.inapp.LINK_RETRIEVAL")
    val inappReceiver = InAppReciever(reactApplicationContext)
    reactApplicationContext.currentActivity?.registerReceiver(inappReceiver, inappFilter)

  }

  @ReactMethod
  fun onMessageRecieved(data: ReadableMap) {
    try {
      DengageManager.getInstance(reactApplicationContext)
        .onMessageReceived(convertMapToJson(data)?.getJSONObject("data")
          ?.let { toMap(it) } as MutableMap<String, String>?)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  class NotifReciever(reactAppContext: ReactApplicationContext) : NotificationReceiver() {
    var reactApplicationContext: ReactApplicationContext? = reactAppContext

    override fun onReceive(context: Context, intent: Intent?) {
      val intentAction = intent?.action
      if (intentAction != null) {
        when (intentAction.hashCode()) {
          -825236177 -> {
            if (intentAction == "com.dengage.push.intent.RECEIVE") {
              Log.d("den/react-native", "received new push.")
              val message: Message = intent.getExtras()?.let { Message.createFromIntent(it) }!!
              sendEvent(
                "onNotificationReceived",
                convertJsonToMap(JSONObject(Gson().toJson(message)))!!,
                reactApplicationContext
              )
            }
          }
          -520704162 -> {
            Dengage.getLastPushPayload()
            // intentAction == "com.dengage.push.intent.RECEIVE"
            Log.d("den/react-native", "push is clicked.")
            val message: Message = intent.getExtras()?.let { Message.createFromIntent(it) }!!
            sendEvent(
              "onNotificationClicked",
              convertJsonToMap(JSONObject(Gson().toJson(message)))!!,
              reactApplicationContext
            )
          }
        }
      }
    }
  }
  class InAppReciever(reactAppContext: ReactApplicationContext) : InAppBroadcastReceiver() {
    var reactApplicationContext: ReactApplicationContext? = reactAppContext
    override fun onReceive(context: Context?, intent: Intent?) {
      //retrieveInAppLink
      val jsonObject = JSONObject()
      jsonObject.put("targetUrl",intent?.extras?.getString("targetUrl"))
      sendEvent(
        "retrieveInAppLink",
        convertJsonToMap(jsonObject)!!,
        reactApplicationContext
      )
    }

  }

  companion object {
    var reactContext: ReactApplicationContext? = null

    fun sendEvent(
      eventName: String,
      data: WritableMap,
      reactAppContext: ReactApplicationContext? = null
    ) {
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


  @ReactMethod
  fun stopGeofence() {
    try {
      DengageRNCoordinator.sharedInstance.dengageManager?.stopGeofence()
    } catch (ex: Exception) {
      print(ex)
    }
  }


  @ReactMethod
  fun requestLocationPermissions() {
    try {
      DengageRNCoordinator.sharedInstance.dengageManager?.requestLocationPermissions(currentActivity as AppCompatActivity)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun startGeofence() {
    try {
      DengageRNCoordinator.sharedInstance.dengageManager?.startGeofence()
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun resetAppBadge() {
    try {
      AppUtils.resetBadgeCounterOfPushMessages(context = reactApplicationContext)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
  fun setCartItemCount(count: String) {
    Dengage.setCartItemCount(count)
  }

  /**
   * Set cart amount for using in real time in app comparisons
   */
  @ReactMethod
  fun setCartAmount(amount: String) {
    Dengage.setCartAmount(amount)
  }

  /**
   * Set state for using in real time in app comparisons
   */
  @ReactMethod
  fun setState(name: String) {
    Dengage.setState(name)
  }

  /**
   * Set city for using in real time in app comparisons
   */
  @ReactMethod
  fun setCity(name: String) {
    Dengage.setCity(name)
  }

  @ReactMethod
  fun showRealTimeInApp(
    screenName: String ,
    data: ReadableMap
  ) {
    if((toMap(data) as HashMap<String, String>).isEmpty()) {
      Dengage.showRealTimeInApp(currentActivity as AppCompatActivity,
        screenName,
        null)
    }
    else{
      Dengage.showRealTimeInApp(currentActivity as AppCompatActivity,
        screenName,
        toMap(data) as HashMap<String, String>)
    }
  }

  /**
   * Set category path for using in real time in app comparisons
   */
  @ReactMethod
  fun setCategoryPath(path: String) {
    Dengage.setCategoryPath(path)
  }

  @ReactMethod
  fun setPartnerDeviceId(adid: String) {
    try {
      Dengage.setPartnerDeviceId(adid)
    } catch (ex: Exception) {
      print(ex)
    }
  }

  @ReactMethod
   fun getLastPushPayload (promise: Promise) {
    try {
      val pushPayload = Dengage.getLastPushPayload()
      promise.resolve(pushPayload)
      return
    } catch (ex: Exception) {
      promise.resolve(ex.message)
    }
  }
}
