package com.dengagetech.reactnativedengage

import com.facebook.react.bridge.ReadableMap

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
