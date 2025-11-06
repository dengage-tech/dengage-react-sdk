package com.dengagetech.reactnativedengage

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.dengage.sdk.Dengage
import com.dengage.sdk.callback.DengageCallback
import com.dengage.sdk.callback.DengageError
import com.dengage.sdk.domain.inboxmessage.model.InboxMessage
import com.dengage.sdk.domain.inappmessage.model.Cart
import com.dengage.sdk.domain.inappmessage.model.CartItem
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

    @ReactMethod
    fun setCategoryPath(path: String) {
        Dengage.setCategoryPath(path)
    }

    @ReactMethod
    fun setCartItemCount(count: String) {
        Dengage.setCartItemCount(count)
    }

    @ReactMethod
    fun setCartAmount(amount: String) {
        Dengage.setCartAmount(amount)
    }

    @ReactMethod
    fun setState(name: String) {
        Dengage.setState(name)
    }

    @ReactMethod
    fun setCity(name: String) {
        Dengage.setCity(name)
    }

    @ReactMethod
    fun showRealTimeInApp(screenName: String, params: ReadableMap?) {
        val activity = currentActivity ?: return
        Dengage.showRealTimeInApp(
            activity,
            screenName,
            params.toHashMap()
        )
    }














    // Inbox Messages

    @ReactMethod
    fun getInboxMessages(offset: Int, limit: Int, promise: Promise) {

        Dengage.getInboxMessages(
            limit,
            offset,
            object : DengageCallback<MutableList<InboxMessage>> {
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

    @ReactMethod
    fun deleteAllInboxMessages(promise: Promise) {
        try {
            Dengage.deleteAllInboxMessages()
            promise.resolve(true)
        } catch (ex: Exception) {
            promise.reject(ex)
        }
    }

    @ReactMethod
    fun setAllInboxMessageAsClicked(promise: Promise) {
        try {
            Dengage.setAllInboxMessagesAsClicked()
            promise.resolve(true)
        } catch (ex: Exception) {
            promise.reject(ex)
        }
    }

    @ReactMethod
    fun getIntegrationKey(promise: Promise) {
        try {
            val integrationKey = Dengage.getSubscription()?.integrationKey ?: ""
            promise.resolve(integrationKey)
        } catch (ex: Exception) {
            promise.reject(ex)
        }
    }

    @ReactMethod
    fun sendCustomEvent(eventTable: String, key: String, parameters: ReadableMap) {
        try {
            val paramsMap = parameters.toHashMap()
            Dengage.sendCustomEvent(
                tableName = eventTable,
                key = key,
                data = paramsMap as HashMap<String, Any>
            )
        } catch (ex: Exception) {
            Log.e("DengageRN", "Error sending custom event", ex)
        }
    }

    @ReactMethod
    fun sendDeviceEvent(tableName: String, data: ReadableMap) {
        try {
            val dataMap = data.toHashMap()
            Dengage.sendDeviceEvent(
                tableName = tableName,
                data = dataMap as HashMap<String, Any>
            )
        } catch (ex: Exception) {
            Log.e("DengageRN", "Error sending device event", ex)
        }
    }

    @ReactMethod
    fun setCart(cart: ReadableMap, promise: Promise) {
        try {
            val itemsArray = cart.getArray("items")
            if (itemsArray != null) {
                val cartItems = mutableListOf<CartItem>()
                for (i in 0 until itemsArray.size()) {
                    val itemMap = itemsArray.getMap(i)
                    if (itemMap != null) {
                        val productId = itemMap.getString("productId") ?: itemMap.getString("product_id") ?: ""
                        val productVariantId = itemMap.getString("productVariantId") ?: itemMap.getString("product_variant_id") ?: ""
                        val categoryPath = itemMap.getString("categoryPath") ?: itemMap.getString("category_path") ?: ""
                        val price = if (itemMap.hasKey("price")) itemMap.getInt("price") else 0
                        val discountedPrice = if (itemMap.hasKey("discountedPrice")) {
                            itemMap.getInt("discountedPrice")
                        } else if (itemMap.hasKey("discounted_price")) {
                            itemMap.getInt("discounted_price")
                        } else {
                            0
                        }
                        val hasDiscount = if (itemMap.hasKey("hasDiscount")) {
                            itemMap.getBoolean("hasDiscount")
                        } else if (itemMap.hasKey("has_discount")) {
                            itemMap.getBoolean("has_discount")
                        } else {
                            false
                        }
                        val hasPromotion = if (itemMap.hasKey("hasPromotion")) {
                            itemMap.getBoolean("hasPromotion")
                        } else if (itemMap.hasKey("has_promotion")) {
                            itemMap.getBoolean("has_promotion")
                        } else {
                            false
                        }
                        val quantity = if (itemMap.hasKey("quantity")) itemMap.getInt("quantity") else 0
                        
                        val attributesMap = itemMap.getMap("attributes")
                        val attributes = mutableMapOf<String, String>()
                        if (attributesMap != null) {
                            val iterator = attributesMap.keySetIterator()
                            while (iterator.hasNextKey()) {
                                val key = iterator.nextKey()
                                val value = attributesMap.getString(key) ?: ""
                                attributes[key] = value
                            }
                        }
                        
                        val cartItem = CartItem(
                            productId = productId,
                            productVariantId = productVariantId,
                            categoryPath = categoryPath,
                            price = price,
                            discountedPrice = discountedPrice,
                            hasDiscount = hasDiscount,
                            hasPromotion = hasPromotion,
                            quantity = quantity,
                            attributes = attributes
                        )
                        cartItems.add(cartItem)
                    }
                }
                val cartObj = Cart(items = cartItems)
                Dengage.setCart(cartObj)
                promise.resolve(true)
            } else {
                promise.reject("INVALID_CART", "Cart items not found")
            }
        } catch (ex: Exception) {
            promise.reject(ex)
        }
    }

    @ReactMethod
    fun getCart(promise: Promise) {
        try {
            val cart = Dengage.getCart()
            val map = WritableNativeMap()
            
            // Convert items
            val itemsArray = WritableNativeArray()
            for (item in cart.items) {
                val itemMap = WritableNativeMap()
                itemMap.putString("productId", item.productId)
                itemMap.putString("productVariantId", item.productVariantId)
                itemMap.putString("categoryPath", item.categoryPath)
                itemMap.putInt("price", item.price)
                itemMap.putInt("discountedPrice", item.discountedPrice)
                itemMap.putBoolean("hasDiscount", item.hasDiscount)
                itemMap.putBoolean("hasPromotion", item.hasPromotion)
                itemMap.putInt("quantity", item.quantity)
                
                val attributesMap = WritableNativeMap()
                for ((key, value) in item.attributes) {
                    attributesMap.putString(key, value)
                }
                itemMap.putMap("attributes", attributesMap)
                
                itemMap.putInt("effectivePrice", item.effectivePrice)
                itemMap.putInt("lineTotal", item.lineTotal)
                itemMap.putInt("discountedLineTotal", item.discountedLineTotal)
                itemMap.putInt("effectiveLineTotal", item.effectiveLineTotal)
                
                val segmentsArray = WritableNativeArray()
                for (segment in item.categorySegments) {
                    segmentsArray.pushString(segment)
                }
                itemMap.putArray("categorySegments", segmentsArray)
                itemMap.putString("categoryRoot", item.categoryRoot)
                
                itemsArray.pushMap(itemMap)
            }
            map.putArray("items", itemsArray)
            
            // Convert summary
            val summaryMap = WritableNativeMap()
            summaryMap.putString("currency", cart.summary.currency)
            summaryMap.putDouble("updatedAt", cart.summary.updatedAt.toDouble())
            summaryMap.putInt("linesCount", cart.summary.linesCount)
            summaryMap.putInt("itemsCount", cart.summary.itemsCount)
            summaryMap.putInt("subtotal", cart.summary.subtotal)
            summaryMap.putInt("discountedSubtotal", cart.summary.discountedSubtotal)
            summaryMap.putInt("effectiveSubtotal", cart.summary.effectiveSubtotal)
            summaryMap.putBoolean("anyDiscounted", cart.summary.anyDiscounted)
            summaryMap.putBoolean("allDiscounted", cart.summary.allDiscounted)
            summaryMap.putInt("minPrice", cart.summary.minPrice)
            summaryMap.putInt("maxPrice", cart.summary.maxPrice)
            summaryMap.putInt("minEffectivePrice", cart.summary.minEffectivePrice)
            summaryMap.putInt("maxEffectivePrice", cart.summary.maxEffectivePrice)
            
            val categoriesMap = WritableNativeMap()
            for ((key, value) in cart.summary.categories) {
                categoriesMap.putInt(key, value)
            }
            summaryMap.putMap("categories", categoriesMap)
            
            map.putMap("summary", summaryMap)
            
            promise.resolve(map)
        } catch (ex: Exception) {
            promise.reject(ex)
        }
    }

    @ReactMethod
    fun getSdkParameters(promise: Promise) {
        try {
            val sdkParams = Dengage.getSdkParameters()
            if (sdkParams != null) {
                val map = WritableNativeMap()
                
                map.putString("appId", sdkParams.appId)
                if (sdkParams.accountId != null) {
                    map.putInt("accountId", sdkParams.accountId!!)
                }
                map.putString("accountName", sdkParams.accountName)
                map.putBoolean("eventsEnabled", sdkParams.eventsEnabled)
                map.putBoolean("inboxEnabled", sdkParams.inboxEnabled ?: false)
                map.putBoolean("inAppEnabled", sdkParams.inAppEnabled ?: false)
                map.putBoolean("subscriptionEnabled", sdkParams.subscriptionEnabled ?: false)
                map.putInt("inAppFetchIntervalInMin", sdkParams.inAppFetchIntervalInMin ?: 0)
                map.putInt("expiredMessagesFetchIntervalInMin", sdkParams.expiredMessagesFetchIntervalInMin ?: 0)
                map.putInt("inAppMinSecBetweenMessages", sdkParams.inAppMinSecBetweenMessages ?: 0)
                map.putDouble("lastFetchTimeInMillis", sdkParams.lastFetchTimeInMillis.toDouble())
                map.putBoolean("appTrackingEnabled", sdkParams.appTrackingEnabled)
                map.putBoolean("realTimeInAppEnabled", sdkParams.realTimeInAppEnabled ?: false)
                map.putInt("realTimeInAppFetchIntervalInMinutes", sdkParams.realTimeInAppFetchIntervalInMinutes ?: 0)
                map.putInt("realTimeInAppSessionTimeoutMinutes", sdkParams.realTimeInAppSessionTimeoutMinutes ?: 0)
                map.putString("surveyCheckEndpoint", sdkParams.surveyCheckEndpoint)
                
                // Convert debugDeviceIds
                if (sdkParams.debugDeviceIds != null) {
                    val debugIdsArray = WritableNativeArray()
                    for (id in sdkParams.debugDeviceIds) {
                        debugIdsArray.pushString(id)
                    }
                    map.putArray("debugDeviceIds", debugIdsArray)
                }
                
                // Convert eventMappings
                if (sdkParams.eventMappings != null) {
                    val eventMappingsArray = WritableNativeArray()
                    for (mapping in sdkParams.eventMappings!!) {
                        val mappingMap = WritableNativeMap()
                        mappingMap.putString("eventTableName", mapping.eventTableName)
                        
                        // Convert eventTypeDefinitions
                        if (mapping.eventTypeDefinitions != null) {
                            val typeDefsArray = WritableNativeArray()
                            for (typeDef in mapping.eventTypeDefinitions!!) {
                                val typeDefMap = WritableNativeMap()
                                typeDefMap.putInt("eventTypeId", typeDef.eventTypeId ?: 0)
                                typeDefMap.putString("eventType", typeDef.eventType)
                                typeDefMap.putString("logicOperator", typeDef.logicOperator)
                                typeDefMap.putBoolean("enableClientHistory", typeDef.enableClientHistory ?: false)
                                
                                // Convert filterConditions
                                if (typeDef.filterConditions != null) {
                                    val filterArray = WritableNativeArray()
                                    for (filter in typeDef.filterConditions!!) {
                                        val filterMap = WritableNativeMap()
                                        filterMap.putString("fieldName", filter.fieldName)
                                        filterMap.putString("operator", filter.operator)
                                        if (filter.values != null) {
                                            val valuesArray = WritableNativeArray()
                                            for (value in filter.values!!) {
                                                valuesArray.pushString(value)
                                            }
                                            filterMap.putArray("values", valuesArray)
                                        }
                                        filterArray.pushMap(filterMap)
                                    }
                                    typeDefMap.putArray("filterConditions", filterArray)
                                }
                                
                                // Convert clientHistoryOptions
                                if (typeDef.clientHistoryOptions != null) {
                                    val clientHistoryMap = WritableNativeMap()
                                    clientHistoryMap.putInt("maxEventCount", typeDef.clientHistoryOptions!!.maxEventCount ?: 0)
                                    clientHistoryMap.putInt("timeWindowInMinutes", typeDef.clientHistoryOptions!!.timeWindowInMinutes ?: 0)
                                    typeDefMap.putMap("clientHistoryOptions", clientHistoryMap)
                                }
                                
                                // Convert attributes
                                if (typeDef.attributes != null) {
                                    val attributesArray = WritableNativeArray()
                                    for (attr in typeDef.attributes!!) {
                                        val attrMap = WritableNativeMap()
                                        attrMap.putString("name", attr.name)
                                        attrMap.putString("dataType", attr.dataType)
                                        attrMap.putString("tableColumnName", attr.tableColumnName)
                                        attributesArray.pushMap(attrMap)
                                    }
                                    typeDefMap.putArray("attributes", attributesArray)
                                }
                                
                                typeDefsArray.pushMap(typeDefMap)
                            }
                            mappingMap.putArray("eventTypeDefinitions", typeDefsArray)
                        }
                        
                        eventMappingsArray.pushMap(mappingMap)
                    }
                    map.putArray("eventMappings", eventMappingsArray)
                }
                
                promise.resolve(map)
            } else {
                promise.resolve(null)
            }
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



    @ReactMethod
    fun registerNotificationListeners() {

        Log.d("den/react-native", "RegisteringNotificationListeners.")

        val filter = IntentFilter()
        filter.addAction("com.dengage.push.intent.RECEIVE")
        filter.addAction("com.dengage.push.intent.OPEN")
        val notifReceiver = NotifReciever(reactApplicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            reactApplicationContext.currentActivity?.registerReceiver(notifReceiver, filter,
                Context.RECEIVER_EXPORTED)
        }
        else {
            ContextCompat.registerReceiver(
                reactApplicationContext,
                notifReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }

    }

    private var listenerCount = 0

    @ReactMethod
    fun addListener(eventName: String) {
        if (listenerCount == 0) {
            Log.d(NAME, "addListener")
            // Set up any upstream listeners or background tasks as necessary
        }

        listenerCount += 1
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        listenerCount -= count
        if (listenerCount == 0) {
            Log.d(NAME, "removeListeners")
            // Remove upstream listeners, stop unnecessary background tasks
        }
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
