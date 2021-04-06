package com.reactnativedengage

import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableType
import com.reactnativedengage.MapUtil.toJSONObject
import com.reactnativedengage.MapUtil.toMap
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/*
  ArrayUtil exposes a set of helper methods for working with
  ReadableArray (by React Native), Object[], and JSONArray.

  MIT License

  Copyright (c) 2020 Marc Mendiola

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 */



object ArrayUtil {
  @Throws(JSONException::class)
  fun toJSONArray(readableArray: ReadableArray?): JSONArray {
    val jsonArray = JSONArray()
    for (i in 0 until readableArray!!.size()) {
      val type = readableArray.getType(i)
      when (type) {
        ReadableType.Null -> jsonArray.put(i, null)
        ReadableType.Boolean -> jsonArray.put(i, readableArray.getBoolean(i))
        ReadableType.Number -> jsonArray.put(i, readableArray.getDouble(i))
        ReadableType.String -> jsonArray.put(i, readableArray.getString(i))
        ReadableType.Map -> jsonArray.put(i, toJSONObject(readableArray.getMap(i)))
        ReadableType.Array -> jsonArray.put(i, toJSONArray(readableArray.getArray(i)))
      }
    }
    return jsonArray
  }

  @Throws(JSONException::class)
  fun toArray(jsonArray: JSONArray): Array<Any?> {
    val array = arrayOfNulls<Any>(jsonArray.length())
    for (i in 0 until jsonArray.length()) {
      var value = jsonArray[i]
      if (value is JSONObject) {
        value = toMap(value)
      }
      if (value is JSONArray) {
        value = toArray(value)
      }
      array[i] = value
    }
    return array
  }

  fun toArray(readableArray: ReadableArray?): Array<Any?> {
    val array = arrayOfNulls<Any>(readableArray!!.size())
    for (i in 0 until readableArray.size()) {
      val type = readableArray.getType(i)
      when (type) {
        ReadableType.Null -> array[i] = null
        ReadableType.Boolean -> array[i] = readableArray.getBoolean(i)
        ReadableType.Number -> array[i] = readableArray.getDouble(i)
        ReadableType.String -> array[i] = readableArray.getString(i)
        ReadableType.Map -> array[i] = toMap(readableArray.getMap(i))
        ReadableType.Array -> array[i] = toArray(readableArray.getArray(i))
      }
    }
    return array
  }

//  fun toWritableArray(array: Array<Any?>?): WritableArray {
//    val writableArray = Arguments.createArray()
//    for (i in array!!.indices) {
//      val value = array[i]
//      if (value == null) {
//        writableArray.pushNull()
//      }
//      if (value is Boolean) {
//        writableArray.pushBoolean((value as Boolean?)!!)
//      }
//      if (value is Double) {
//        writableArray.pushDouble((value as Double?)!!)
//      }
//      if (value is Int) {
//        writableArray.pushInt((value as Int?)!!)
//      }
//      if (value is String) {
//        writableArray.pushString(value as String?)
//      }
//      if (value is Map<*, *>) {
//        writableArray.pushMap(toWritableMap(value as Map<String?, Any?>?))
//      }
//      if (value.javaClass.isArray()) {
//        writableArray.pushArray(toWritableArray(value as Array<Any?>?))
//      }
//    }
//    return writableArray
//  }
}
