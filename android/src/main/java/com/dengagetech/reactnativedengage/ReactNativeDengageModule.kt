package com.dengagetech.reactnativedengage

import android.app.Activity
import com.dengage.sdk.Dengage
import com.dengage.sdk.callback.DengageCallback
import com.dengage.sdk.callback.DengageError
import com.dengage.sdk.domain.inboxmessage.model.InboxMessage
import com.facebook.react.bridge.*

class ReactNativeDengageModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String = NAME

  // Push Notifications

  @ReactMethod
  fun promptForPushNotifications() {
    val currentActivity = currentActivity ?: return
    Dengage.requestNotificationPermission(currentActivity)
  }

  @ReactMethod
  fun setContactKey(contactKey: String?) {
    Dengage.setContactKey(contactKey)
  }

  @ReactMethod
  fun getContactKey(promise: Promise) {
    try {
      val contactKey = Dengage.getSubscription()?.contactKey
      promise.resolve(contactKey)
    } catch (ex: Exception) {
      promise.reject(ex)
    }
  }

  @ReactMethod
  fun setUserPermission(permission: Boolean) {
    Dengage.setUserPermission(permission)
  }

  @ReactMethod
  fun getSubscription(promise: Promise) {
    try {
      val original = Dengage.getSubscription()
      val map = WritableNativeMap()
      if (original != null) {
        map.putString("integrationKey", original.integrationKey)
        map.putString("token", original.token)
        map.putString("appVersion", original.appVersion)
        map.putString("sdkVersion", original.sdkVersion)
        map.putString("deviceId", original.deviceId)
        map.putString("advertisingId", original.advertisingId)
        map.putString("carrierId", original.carrierId)
        map.putString("contactKey", original.contactKey)
        map.putBoolean("permission", original.permission ?: false)
        map.putBoolean("trackingPermission", original.trackingPermission)
        map.putString("tokenType", original.tokenType)
        map.putString("webSubscription", original.webSubscription)
        map.putString("testGroup", original.testGroup)
        map.putString("country", original.country)
        map.putString("language", original.language)
        map.putString("timezone", original.timezone)
        map.putString("partnerDeviceId", original.partnerDeviceId)
        map.putString("locationPermission", original.locationPermission)
      }
      promise.resolve(map)
    } catch (ex: Exception) {
      promise.reject(ex)
    }
  }

  // Inapp Notifications

  @ReactMethod
  fun setNavigation(screenName: String?) {
    currentActivity?.let {
      Dengage.setNavigation(it, screenName)
    }
  }

  @ReactMethod
  fun setInAppDeviceInfo(key: String, value: String) {
    Dengage.setInAppDeviceInfo(key, value)
  }

  @ReactMethod
  fun clearInAppDeviceInfo() {
    Dengage.clearInAppDeviceInfo()
  }

  @ReactMethod
  fun getInAppDeviceInfo(promise: Promise) {
    try {
      val inAppDeviceInfo = Dengage.getInAppDeviceInfo()
      val map = WritableNativeMap()
      for ((key, value) in inAppDeviceInfo) {
        map.putString(key, value)
      }
      promise.resolve(map)
    } catch (ex: Exception) {
      promise.reject(ex)
    }
  }

  // Inbox Messages

  @ReactMethod
  fun getInboxMessages(offset: Int, limit: Int, promise: Promise) {

    Dengage.getInboxMessages(limit, offset, object : DengageCallback<MutableList<InboxMessage>> {
      override fun onResult(result: MutableList<InboxMessage>) {
        try {
          val arr = WritableNativeArray()
          for (message in result) {
            val map = WritableNativeMap()
            map.putString("id", message.id)
            map.putString("title", message.data.title ?: "")
            map.putString("message", message.data.message ?: "")
            map.putString("mediaURL", message.data.mediaUrl ?: "")
            map.putString("targetUrl", message.data.targetUrl ?: "")
            map.putString("receiveDate", message.data.receiveDate ?: "")
            map.putBoolean("isClicked", message.isClicked ?: false)

            val carouselArr = WritableNativeArray()
            message.data.carouselItems?.forEach { carousel ->
              val carouselMap = WritableNativeMap()
              carouselMap.putString("id", carousel.id)
              carouselMap.putString("title", carousel.title)
              carouselMap.putString("descriptionText", carousel.description)
              carouselMap.putString("mediaUrl", carousel.mediaUrl)
              carouselMap.putString("targetUrl", carousel.targetUrl)
              carouselArr.pushMap(carouselMap)
            }
            map.putArray("carouselItems", carouselArr)

            arr.pushMap(map)
          }
          promise.resolve(arr)
        } catch (ex: Exception) {
          promise.reject(ex)
        }
      }

      override fun onError(error: DengageError) {
        promise.reject(Error(error.errorMessage))
      }
    })
  }

  @ReactMethod
  fun deleteInboxMessage(id: String, promise: Promise) {
    try {
      Dengage.deleteInboxMessage(id)
      promise.resolve(true)
    } catch (ex: Exception) {
      promise.reject(ex)
    }
  }

  @ReactMethod
  fun setInboxMessageAsClicked(id: String, promise: Promise) {
    try {
      Dengage.setInboxMessageAsClicked(id)
      promise.resolve(true)
    } catch (ex: Exception) {
      promise.reject(ex)
    }
  }

  // Geofence

  @ReactMethod
  fun requestLocationPermissions() {
    val activity = currentActivity ?: return
    try {
      val clazz = Class.forName("com.dengage.geofence.DengageGeofence")
      val instance = clazz.getField("INSTANCE").get(null) // Kotlin object singleton instance
      val method = clazz.getMethod("requestLocationPermissions", Activity::class.java)
      method.invoke(instance, activity)
    } catch (e: ClassNotFoundException) {
      println("DengageGeofence library could not be found")
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }


  companion object {
    const val NAME = "DengageRN"
  }


  /*



  fun requestLocationPermissions() {
    try {
      val clazz = Class.forName("com.dengage.geofence.DengageGeofence")
      val instance = clazz.getDeclaredConstructor().newInstance()
      val method = clazz.getMethod("requestLocationPermissions")
      method.invoke(instance)
    } catch (e: ClassNotFoundException) {
      println("DengageGeofence kütüphanesi bulunamadı")
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
  */
}
