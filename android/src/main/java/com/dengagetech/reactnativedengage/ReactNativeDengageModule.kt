package com.dengagetech.reactnativedengage

import com.dengage.sdk.Dengage
//import com.dengage.geofence.DengageGeofence
import com.facebook.react.bridge.*

class ReactNativeDengageModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String = NAME

  @ReactMethod
  fun multiply(a: Double, b: Double, promise: Promise) {
    promise.resolve(a * b)
  }

  @ReactMethod
  fun promptForPushNotifications() {
    val currentActivity = currentActivity ?: return
    Dengage.requestNotificationPermission(currentActivity)
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

  @ReactMethod
  fun setContactKey(contactKey: String) {
    Dengage.setContactKey(contactKey)
  }

  @ReactMethod
  fun getContactKey(promise: Promise) {
    try {
      val contactKey = Dengage.getSubscription()?.contactKey
      promise.resolve(null)
    } catch (ex: Exception) {
      promise.reject(ex)
    }
  }

  companion object {
    const val NAME = "DengageRN"
  }
}
