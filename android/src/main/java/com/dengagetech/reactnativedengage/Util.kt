package com.dengagetech.reactnativedengage

import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun ReadableMap?.toHashMap(): HashMap<String, String>? {
  if (this == null) return null

  val hashMap = HashMap<String, String>()
  val iterator = this.keySetIterator()

  while (iterator.hasNextKey()) {
    val key = iterator.nextKey()
    val value = this.getString(key)
    if (value != null) {
      hashMap[key] = value
    }
  }
  return hashMap
}


@Throws(JSONException::class)
fun convertJsonToMap(jsonObject: JSONObject): WritableMap? {
    val map: WritableMap = WritableNativeMap()
    val iterator = jsonObject.keys()
    while (iterator.hasNext()) {
        val key = iterator.next()
        val value = jsonObject[key]
        if (value is JSONObject) {
            map.putMap(key, convertJsonToMap(value))
        } else if (value is JSONArray) {
            map.putArray(key, convertJsonToArray(value))
            if ("option_values" == key) {
                map.putArray("options", convertJsonToArray(value))
            }
        } else if (value is Boolean) {
            map.putBoolean(key, value)
        } else if (value is Int) {
            map.putInt(key, value)
        } else if (value is Double) {
            map.putDouble(key, value)
        } else if (value is String) {
            map.putString(key, value)
        } else {
            map.putString(key, value.toString())
        }
    }
    return map
}

@Throws(JSONException::class)
fun convertJsonToArray(jsonArray: JSONArray): WritableArray? {
    val array: WritableArray = WritableNativeArray()
    for (i in 0 until jsonArray.length()) {
        val value = jsonArray[i]
        if (value is JSONObject) {
            array.pushMap(convertJsonToMap(value))
        } else if (value is JSONArray) {
            array.pushArray(convertJsonToArray(value))
        } else if (value is Boolean) {
            array.pushBoolean(value)
        } else if (value is Int) {
            array.pushInt(value)
        } else if (value is Double) {
            array.pushDouble(value)
        } else if (value is String) {
            array.pushString(value)
        } else {
            array.pushString(value.toString())
        }
    }
    return array
}
