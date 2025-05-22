package com.dengagetech.reactnativedengage

import android.content.Context
import android.content.Intent
import android.util.Log
import com.dengage.sdk.Dengage
import com.dengage.sdk.domain.push.model.Message
import com.dengage.sdk.push.NotificationReceiver
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.google.gson.Gson
import org.json.JSONObject

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

    private fun sendEvent(
        eventName: String,
        data: WritableMap,
        reactAppContext: ReactApplicationContext? = null
    ) {
        Log.d("sendingEvent", eventName)
        if (reactApplicationContext != null) {
            reactApplicationContext
                ?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                ?.emit(eventName, data)
        } else if (reactAppContext != null) {
            reactAppContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                ?.emit(eventName, data)
        } else {
            Log.d("no reactContext.", "unable to have a react context.")
        }
    }
}
