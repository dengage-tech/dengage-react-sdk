package com.dengagetech.reactnativedengage

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun ReadableMap?.toHashMapAny(): HashMap<String, Any> {
    val result = HashMap<String, Any>()
    if (this == null) return result
    val iterator = keySetIterator()
    while (iterator.hasNextKey()) {
        val key = iterator.nextKey()
        when (getType(key)) {
            ReadableType.Null -> {}
            ReadableType.Boolean -> result[key] = getBoolean(key)
            ReadableType.Number -> {
                val num = getDouble(key)
                if (num == num.toLong().toDouble()) result[key] = num.toLong()
                else result[key] = num
            }
            ReadableType.String -> result[key] = getString(key) ?: ""
            ReadableType.Map -> getMap(key)?.toHashMapAny()?.let { result[key] = it }
            ReadableType.Array -> getArray(key)?.toArrayList()?.let { result[key] = it }
        }
    }
    return result
}

fun ReadableArray?.toArrayList(): ArrayList<Any> {
    val result = ArrayList<Any>()
    if (this == null) return result
    for (i in 0 until size()) {
        when (getType(i)) {
            ReadableType.Null -> {}
            ReadableType.Boolean -> result.add(getBoolean(i))
            ReadableType.Number -> {
                val num = getDouble(i)
                if (num == num.toLong().toDouble()) result.add(num.toLong())
                else result.add(num)
            }
            ReadableType.String -> result.add(getString(i) ?: "")
            ReadableType.Map -> result.add(getMap(i)?.toHashMapAny() ?: HashMap<String, Any>())
            ReadableType.Array -> result.add(getArray(i)?.toArrayList() ?: ArrayList<Any>())
        }
    }
    return result
}

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
