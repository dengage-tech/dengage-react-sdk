package com.dengagetech.reactnativedengage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

class InAppLinkReceiver(private val reactApplicationContext: ReactApplicationContext) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val targetUrl = intent?.extras?.getString("targetUrl")
            val map: WritableMap = Arguments.createMap()
            map.putString("targetUrl", targetUrl)
            sendEvent("retrieveInAppLink", map)
        } catch (ex: Exception) {
            Log.e("DengageRN", "InAppLinkReceiver error", ex)
        }
    }

    private fun sendEvent(eventName: String, data: WritableMap) {
        Log.d("sendingEvent", eventName)
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            ?.emit(eventName, data)
    }
}
