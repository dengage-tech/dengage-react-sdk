import React
import Dengage
#if canImport(DengageGeofence)
import DengageGeofence
#endif

@objc(DengageRN)
class ReactNativeDengage: RCTEventEmitter {
    
    // MARK: - Push Notifications
    
    @objc
    func promptForPushNotifications() {
        Dengage.promptForPushNotifications()
    }
    
    @objc(setUserPermission:)
    func setUserPermission(permission: Bool) {
        Dengage.setUserPermission(permission: permission)
    }
    
    @objc
    func getUserPermission(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        resolve(Dengage.getPermission())
    }
    
    @objc(setContactKey:)
    func setContactKey(contactKey: String?) {
        Dengage.setContactKey(contactKey: contactKey)
    }
    
    @objc
    func getContactKey(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        resolve(Dengage.getContactKey())
    }
    
    @objc
    func getSubscription(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        let integrationKey =
            DengageRNCoordinator.staticInstance.integrationKey ??
            Dengage.getIntegrationKey() ??
            ""
        let subscription = Subscription(
            integrationKey: integrationKey,
            token: Dengage.getDeviceToken(),
            appVersion: "",
            sdkVersion: Dengage.getSdkVersion() ?? "",
            deviceId: Dengage.getDeviceId(),
            advertisingId: "",
            carrierId: "",
            contactKey: Dengage.getContactKey(),
            permission: Dengage.getPermission(),
            trackingPermission: false,
            tokenType: "",
            webSubscription: "",
            testGroup: "",
            country: "",
            language: "",
            timezone: "",
            partnerDeviceId: "",
            locationPermission: ""
        )
        resolve(subscription.toDictionary())
    }
    
    // MARK: - Inapp Notifications
    
    @objc(setNavigation)
    func setNavigation() {
        Dengage.setNavigation()
    }
    
    @objc(setNavigationWithName:)
    func setNavigationWithName(screenName: NSString) {
        Dengage.setNavigation(screenName: screenName as String)
    }
    
    @objc(setInAppDeviceInfo:withValue:)
    func setInAppDeviceInfo(key: String, value: String) {
        Dengage.setInAppDeviceInfo(key: key, value: value)
    }
    
    @objc(clearInAppDeviceInfo)
    func clearInAppDeviceInfo() {
        Dengage.clearInAppDeviceInfo()
    }
    
    @objc
    func getInAppDeviceInfo(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        resolve(Dengage.getInAppDeviceInfo())
    }
    
    @objc(setCategoryPath:)
    func setCategoryPath(path: NSString) {
        Dengage.setCategory(path: path as String)
    }
    
    @objc(setCartItemCount:)
    func setCartItemCount(count: NSString) {
        Dengage.setCart(itemCount: count as String)
    }
    
    @objc(setCartAmount:)
    func setCartAmount(amount: NSString) {
        Dengage.setCart(amount: amount as String)
    }
    
    @objc(setState:)
    func setState(state: NSString) {
        Dengage.setState(name: state as String)
    }
    
    @objc(setCity:)
    func setCity(city: NSString) {
        Dengage.setCity(name: city as String)
    }
    
    @objc(showRealTimeInApp:withParams:)
    func showRealTimeInApp (_ screenName: NSString, params: NSDictionary) -> Void {
        Dengage.showRealTimeInApp(screenName: screenName as String , params: params as? Dictionary<String, String>)
    }
    
    // MARK: - Inbox Messages
    
    @objc(getInboxMessages:limit:resolve:reject:)
    func getInboxMessages(offset: Int = 10, limit: Int = 20, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock){
        var isCalled = false
        Dengage.getInboxMessages(offset: offset, limit: limit) { (result) in
            func safeResolve(_ value: Any?) {
                if !isCalled {
                    isCalled = true
                    resolve(value)
                }
            }
            func safeReject(_ code: String, _ message: String, _ error: Error?) {
                if !isCalled {
                    isCalled = true
                    reject(code, message, error)
                }
            }
            switch result {
            case .success(let resultType):
                var arrDict = [[String: Any]]()
                let formatter = DateFormatter()
                formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                formatter.timeZone = TimeZone(abbreviation: "UTC")
                
                for message in resultType {
                    var carouselArr = [[String: Any]]()
                    var customParamsArr = [[String: Any]]()
                    if let items = message.carouselItems {
                        for carousel in items {
                            carouselArr.append([
                                "id": carousel.id,
                                "title": carousel.title,
                                "descriptionText": carousel.descriptionText,
                                "mediaUrl": carousel.mediaUrl,
                                "targetUrl": carousel.targetUrl
                            ])
                        }
                    }
                    
                    if let items = message.customParameters {
                        for customParams in items {
                            customParamsArr.append([
                                "key": customParams.key,
                                "value": customParams.value,
                                
                            ])
                        }
                    }
                   
                    let dict: [String: Any] = [
                        "id": message.id,
                        "title": message.title ?? "",
                        "message": message.message ?? "",
                        "mediaURL": message.mediaURL ?? "",
                        "targetUrl": message.targetUrl ?? "",
                        "receiveDate": message.receiveDate != nil ? formatter.string(from: message.receiveDate!) : "",
                        "isClicked": message.isClicked,
                        "carouselItems": carouselArr,
                        "customParameters": customParamsArr,
                    ]
                    arrDict.append(dict)
                }
                safeResolve(arrDict)
            case .failure(let error):
                safeReject("error", error.localizedDescription, error)
            }
        }
    }
    
    @objc(deleteInboxMessage:resolve:reject:)
    func deleteInboxMessage(id: NSString, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock){
        Dengage.deleteInboxMessage(with: id as String) { (result) in
            switch result {
            case .success:
                resolve(true)
                break;
            case .failure (let error):
                reject("error", error.localizedDescription , error)
                break;
            }
        }
    }
    
    @objc(setInboxMessageAsClicked:resolve:reject:)
    func setInboxMessageAsClicked(id: NSString, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock){
        Dengage.setInboxMessageAsClicked(with: id as String) { (result) in
            switch result {
            case .success:
                resolve(true)
                break;
            case .failure (let error):
                reject("error", error.localizedDescription , error)
                break;
            }
        }
    }
    
    @objc(deleteAllInboxMessages:reject:)
    func deleteAllInboxMessages(resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock){
        Dengage.deleteAllInboxMessages { (result) in
            switch result {
            case .success:
                resolve(true)
                break;
            case .failure (let error):
                reject("error", error.localizedDescription , error)
                break;
            }
        }
    }
    
    @objc(setAllInboxMessageAsClicked:reject:)
    func setAllInboxMessageAsClicked(resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock){
        Dengage.setAllInboxMessageAsClicked { (result) in
            switch result {
            case .success:
                resolve(true)
                break;
            case .failure (let error):
                reject("error", error.localizedDescription , error)
                break;
            }
        }
    }
    
    
    
    // MARK: - Geofence
    
    @objc(requestLocationPermissions)
    func requestLocationPermissions() {
#if canImport(DengageGeofence)
        DengageGeofence.requestLocationPermissions()
#endif
    }
    
    @objc(startGeofence)
    func startGeofence() {
#if canImport(DengageGeofence)
        DengageGeofence.startGeofence()
#endif
    }
    
    @objc(stopGeofence)
    func stopGeofence() {
#if canImport(DengageGeofence)
        DengageGeofence.stopGeofence()
#endif
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @objc(setIntegrationKey:)
    func setIntegrationKey(key: String) -> Void {
        Dengage.setIntegrationKey(key: key)
    }
    
    @objc(getIntegrationKey:reject:)
    func getIntegrationKey(resolve: @escaping RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        resolve(Dengage.getIntegrationKey())
    }
    
    
    
    @objc(promptForPushNotificationsWitCallback:)
    func promptForPushNotifications(callback: @escaping RCTResponseSenderBlock) {
        Dengage.promptForPushNotifications() { hasPermission in
            callback([hasPermission])
        }
    }
    
    
    
    @objc(registerForRemoteNotifications:)
    func registerForRemoteNotifications(enable: Bool) {
        Dengage.set(permission: true)
    }
    
    @objc
    func getToken(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        resolve(Dengage.getDeviceToken())
    }
    
    
    
    @objc(setToken:)
    func setToken(token: String) {
        Dengage.setToken(token: token)
    }
    
    @objc(setLogStatus:)
    func setLogStatus(isVisible: Bool) {
        Dengage.setLogStatus(isVisible: isVisible)
    }
    

    
    @objc(pageView:)
    func pageView (_ data: NSDictionary) -> Void {
        do {
            try Dengage.pageView(parameters: data as! [String:Any])
        } catch {
            print("Unexpected pageView error: \(error)")
        }
    }
    
    @objc(addToCart:)
    func addToCart (_ data: NSDictionary) -> Void {
        do {
            print(data)
            try Dengage.addToCart(parameters: data as! [String:Any])
        } catch {
            print("Unexpected addToCart error: \(error)")
        }
    }
    
    @objc(removeFromCart:)
    func removeFromCart (_ data: NSDictionary) -> Void {
        do {
            print(data)
            try Dengage.removeFromCart(parameters: data as! [String:Any])
        } catch {
            print("Unexpected removeFromCart error: \(error)")
        }
    }
    
    @objc(viewCart:)
    func viewCart (_ data: NSDictionary) -> Void {
        do {
            print(data)
            try Dengage.viewCart(parameters: data as! [String:Any])
        } catch {
            print("Unexpected viewCart error: \(error)")
        }
    }
    
    @objc(beginCheckout:)
    func beginCheckout (_ data: NSDictionary) -> Void {
        do {
            print(data)
            try Dengage.beginCheckout(parameters: data as! [String:Any])
        } catch {
            print("Unexpected beginCheckout error: \(error)")
        }
    }
    
    @objc(placeOrder:)
    func placeOrder (_ data: NSDictionary) -> Void {
        do {
            print(data)
            try Dengage.order(parameters: data as! [String : Any])
        } catch {
            print("Unexpected placeOrder error: \(error)")
        }
    }
    
    @objc(cancelOrder:)
    func cancelOrder (_ data: NSDictionary) -> Void {
        do {
            print(data)
            try Dengage.cancelOrder(parameters: data as! [String : Any])
            
        } catch {
            print("Unexpected cancelOrder error: \(error)")
        }
    }
    
    @objc(addToWishList:)
    func addToWishList (_ data: NSDictionary) -> Void {
        do {
            print(data)
            try Dengage.addToWithList(parameters: data as! [String : Any])
        } catch {
            print("Unexpected addToWishList error: \(error)")
        }
    }
    
    @objc(removeFromWishList:)
    func removeFromWishList (_ data: NSDictionary) -> Void {
        do {
            print(data)
            try Dengage.removeFromWithList(parameters: data as! [String : Any])
        } catch {
            print("Unexpected removeFromWishList error: \(error)")
        }
    }
    
    @objc(search:)
    func search (_ data: NSDictionary) -> Void {
        do {
            print(data)
            try Dengage.search(parameters: data as! [String : Any])
        } catch {
            print("Unexpected search error: \(error)")
        }
    }
    
    @objc(sendDeviceEvent:withData:)
    func sendDeviceEvent (_ tableName: NSString, withData: NSDictionary) -> Void {
        do {
            print(withData)
            try Dengage.sendCustomEvent(eventTable: tableName as String, parameters: withData as! [String:Any])
        } catch {
            print("Unexpected search error: \(error)")
        }
    }
    
    @objc(sendCustomEvent:withKey:withParameters:)
    func sendCustomEvent (_ eventTable: NSString, withKey: NSString, withParameters: NSDictionary) -> Void {
        do {
            print(withParameters)
            try Dengage.sendCustomEvent(eventTable: eventTable as String, parameters: withParameters as! [String:Any])
        } catch {
            print("Unexpected sendCustomEvent error: \(error)")
        }
    }
    
    @objc(setCart:)
    func setCart(cart: NSDictionary) -> Void {
        guard let items = cart["items"] as? [[String: Any]] else {
            print("Cart items not found")
            return
        }
        
        var cartItems: [CartItem] = []
        for itemDict in items {
            guard let productId = (itemDict["productId"] as? String) ?? (itemDict["product_id"] as? String),
                  let productVariantId = (itemDict["productVariantId"] as? String) ?? (itemDict["product_variant_id"] as? String),
                  let categoryPath = (itemDict["categoryPath"] as? String) ?? (itemDict["category_path"] as? String) else {
                continue
            }
            
            let price = (itemDict["price"] as? Int) ?? ((itemDict["price"] as? NSNumber)?.intValue ?? 0)
            let discountedPrice = (itemDict["discountedPrice"] as? Int) ?? ((itemDict["discounted_price"] as? Int) ?? ((itemDict["discounted_price"] as? NSNumber)?.intValue ?? 0))
            let hasDiscount = (itemDict["hasDiscount"] as? Bool) ?? ((itemDict["has_discount"] as? Bool) ?? false)
            let hasPromotion = (itemDict["hasPromotion"] as? Bool) ?? ((itemDict["has_promotion"] as? Bool) ?? false)
            let quantity = (itemDict["quantity"] as? Int) ?? ((itemDict["quantity"] as? NSNumber)?.intValue ?? 0)
            let attributes = (itemDict["attributes"] as? [String: String]) ?? [:]
            
            let cartItem = CartItem(
                productId: productId,
                productVariantId: productVariantId,
                categoryPath: categoryPath,
                price: price,
                discountedPrice: discountedPrice,
                hasDiscount: hasDiscount,
                hasPromotion: hasPromotion,
                quantity: quantity,
                attributes: attributes
            )
            cartItems.append(cartItem)
        }
        
        let cartObj = Cart(items: cartItems)
        Dengage.setCart(cart: cartObj)
    }
    
    @objc(getCart:reject:)
    func getCart(resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        let cart = Dengage.getCart()
        var cartDict: [String: Any] = [:]
        
        // Convert items
        var itemsArray: [[String: Any]] = []
        for item in cart.items {
            var itemDict: [String: Any] = [:]
            itemDict["productId"] = item.productId
            itemDict["productVariantId"] = item.productVariantId
            itemDict["categoryPath"] = item.categoryPath
            itemDict["price"] = item.price
            itemDict["discountedPrice"] = item.discountedPrice
            itemDict["hasDiscount"] = item.hasDiscount
            itemDict["hasPromotion"] = item.hasPromotion
            itemDict["quantity"] = item.quantity
            itemDict["attributes"] = item.attributes
            itemDict["effectivePrice"] = item.effectivePrice
            itemDict["lineTotal"] = item.lineTotal
            itemDict["discountedLineTotal"] = item.discountedLineTotal
            itemDict["effectiveLineTotal"] = item.effectiveLineTotal
            itemDict["categorySegments"] = item.categorySegments
            itemDict["categoryRoot"] = item.categoryRoot
            itemsArray.append(itemDict)
        }
        cartDict["items"] = itemsArray
        
        // Convert summary using JSON encoding
        do {
            let summaryData = try JSONEncoder().encode(cart.summary)
            if let summaryDict = try JSONSerialization.jsonObject(with: summaryData) as? [String: Any] {
                cartDict["summary"] = summaryDict
            }
        } catch {
            print("Error encoding CartSummary: \(error)")
        }
        
        resolve(cartDict)
    }
    
    @objc(getSdkParameters:reject:)
    func getSdkParameters(resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        if let sdkParams = Dengage.getSdkParameters() {
            do {
                // Use JSON encoding to convert GetSDKParamsResponse to dictionary
                let encoder = JSONEncoder()
                let jsonData = try encoder.encode(sdkParams)
                if var paramsDict = try JSONSerialization.jsonObject(with: jsonData) as? [String: Any] {
                    // Computed properties are not encoded, so we need to access them separately
                    // These are computed properties that return Double values
                    // We'll calculate them from the encoded values if available
                    if let expiredInterval = paramsDict["expiredMessagesFetchIntervalInMin"] as? Int {
                        paramsDict["expiredMessagesFetchIntervalInMin"] = expiredInterval
                    }
                    if let minSecBetween = paramsDict["inAppMinSecBetweenMessages"] as? Int {
                        paramsDict["inAppMinSecBetweenMessages"] = minSecBetween
                    }
                    resolve(paramsDict)
                } else {
                    resolve(nil)
                }
            } catch {
                print("Error encoding GetSDKParamsResponse: \(error)")
                reject("ENCODING_ERROR", error.localizedDescription, error)
            }
        } else {
            resolve(nil)
        }
    }
    
    
    
    
    
    
    @objc(setPartnerDeviceId:)
    func setPartnerDeviceId(adid: NSString) {
        Dengage.setPartnerDeviceId(adid: adid as String)
    }
    
    @objc
    func getLastPushPayload(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        do {
            let pushPayload = try Dengage.getLastPushPayload()
            resolve(pushPayload)
        } catch {
            print("Unexpected pushPayload error: \(error)")
            reject("UNABLE_TO_RETREIVE_payload", error.localizedDescription ?? "Something went wrong", error)
        }
    }
    
    @objc(registerInAppListener)
    func registerInAppListener ()
    {Dengage.handleInAppDeeplink{ url in
        var response = [String:Any?]();
        response["targetUrl"] = url
        print(url)
        super.sendEvent(withName: "retrieveInAppLink", body: [response])
        
    }
    }
    
    @objc(setInAppLinkConfiguration:)
    func setInAppLinkConfiguration(deeplink: String) {
        Dengage.inAppLinkConfiguration(deeplink: deeplink)
    }
    
    @objc
    func getDeviceId(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        let deviceId = Dengage.getDeviceId()
        resolve(deviceId)
    }
    
    @objc(setDevelopmentStatus:)
    func setDevelopmentStatus(isDebug: Bool) {
        Dengage.setDevelopmentStatus(isDebug: isDebug)
    }
    
    @objc(setLanguage:)
    func setLanguage(language: String) {
        Dengage.setLanguage(language: language)
    }
    
    @objc(setDeviceId:)
    func setDeviceId(deviceId: String) {
        Dengage.setDeviceId(applicationIdentifier: deviceId)
    }
    
}


// deprecated
extension ReactNativeDengage {
    
    @objc(registerNotificationListeners)
    func registerNotificationListeners () {
        Dengage.handleNotificationActionBlock { (notificationResponse) in
            var response = [String:Any?]();
            response["actionIdentifier"] = notificationResponse.actionIdentifier
            
            var notification = [String:Any?]()
            notification["date"] = notificationResponse.notification.date.description
            
            var notificationReq = [String:Any?]()
            notificationReq["identifier"] = notificationResponse.notification.request.identifier
            
            if (notificationResponse.notification.request.trigger?.repeats != nil) {
                var notificationReqTrigger = [String:Any?]()
                notificationReqTrigger["repeats"] = notificationResponse.notification.request.trigger?.repeats ?? nil
                notificationReq["trigger"] = notificationReqTrigger
            }
            
            var reqContent = [String:Any?]()
            var contentAttachments = [Any]()
            for attachment in notificationResponse.notification.request.content.attachments {
                var contentAttachment = [String:Any?]()
                contentAttachment["identifier"] = attachment.identifier
                contentAttachment["url"] = attachment.url
                contentAttachment["type"] = attachment.type
                contentAttachments.append(contentAttachment)
            }
            reqContent["badge"] = notificationResponse.notification.request.content.badge
            reqContent["body"] = notificationResponse.notification.request.content.body
            reqContent["categoryIdentifier"] = notificationResponse.notification.request.content.categoryIdentifier
            reqContent["launchImageName"] = notificationResponse.notification.request.content.launchImageName
            // @NSCopying open var sound: UNNotificationSound? { get }
            //reqContent["sound"] = notificationResponse.notification.request.content.sound // this yet ignored, will include later.
            reqContent["subtitle"] = notificationResponse.notification.request.content.subtitle
            reqContent["threadIdentifier"] = notificationResponse.notification.request.content.threadIdentifier
            reqContent["title"] = notificationResponse.notification.request.content.title
            reqContent["userInfo"] = notificationResponse.notification.request.content.userInfo // todo: make sure it is RCTCovertible & doesn't break the code
            if #available(iOS 12.0, *) {
                reqContent["summaryArgument"] = notificationResponse.notification.request.content.summaryArgument
                reqContent["summaryArgumentCount"] = notificationResponse.notification.request.content.summaryArgumentCount
            }
            if #available(iOS 13.0, *) {
                reqContent["targetContentIdentifier"] = notificationResponse.notification.request.content.targetContentIdentifier
            }
            
            
            reqContent["attachments"] = contentAttachments
            notificationReq["content"] = reqContent
            notification["request"] = notificationReq
            response["notification"] = notification
            
            super.sendEvent(withName: "onNotificationClicked", body: [response])
            
            //            callback([response])
        }
    }
    
    @objc(handleNotificationActionBlock:)
    func handleNotificationActionBlock (_ callback: @escaping RCTResponseSenderBlock) {
        Dengage.handleNotificationActionBlock { (notificationResponse) in
            var response = [String:Any?]();
            response["actionIdentifier"] = notificationResponse.actionIdentifier
            
            var notification = [String:Any?]()
            notification["date"] = notificationResponse.notification.date.description
            
            var notificationReq = [String:Any?]()
            notificationReq["identifier"] = notificationResponse.notification.request.identifier
            
            if (notificationResponse.notification.request.trigger?.repeats != nil) {
                var notificationReqTrigger = [String:Any?]()
                notificationReqTrigger["repeats"] = notificationResponse.notification.request.trigger?.repeats ?? nil
                notificationReq["trigger"] = notificationReqTrigger
            }
            
            var reqContent = [String:Any?]()
            var contentAttachments = [Any]()
            for attachment in notificationResponse.notification.request.content.attachments {
                var contentAttachment = [String:Any?]()
                contentAttachment["identifier"] = attachment.identifier
                contentAttachment["url"] = attachment.url
                contentAttachment["type"] = attachment.type
                contentAttachments.append(contentAttachment)
            }
            reqContent["badge"] = notificationResponse.notification.request.content.badge
            reqContent["body"] = notificationResponse.notification.request.content.body
            reqContent["categoryIdentifier"] = notificationResponse.notification.request.content.categoryIdentifier
            reqContent["launchImageName"] = notificationResponse.notification.request.content.launchImageName
            // @NSCopying open var sound: UNNotificationSound? { get }
            //reqContent["sound"] = notificationResponse.notification.request.content.sound // this yet ignored, will include later.
            reqContent["subtitle"] = notificationResponse.notification.request.content.subtitle
            reqContent["threadIdentifier"] = notificationResponse.notification.request.content.threadIdentifier
            reqContent["title"] = notificationResponse.notification.request.content.title
            reqContent["userInfo"] = notificationResponse.notification.request.content.userInfo // todo: make sure it is RCTCovertible & doesn't break the code
            if #available(iOS 12.0, *) {
                reqContent["summaryArgument"] = notificationResponse.notification.request.content.summaryArgument
                reqContent["summaryArgumentCount"] = notificationResponse.notification.request.content.summaryArgumentCount
            }
            if #available(iOS 13.0, *) {
                reqContent["targetContentIdentifier"] = notificationResponse.notification.request.content.targetContentIdentifier
            }
            
            
            reqContent["attachments"] = contentAttachments
            notificationReq["content"] = reqContent
            notification["request"] = notificationReq
            response["notification"] = notification
            
            callback([response])
            
            
            
        }
    }
}