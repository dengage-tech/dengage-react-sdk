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
  
  // MARK: - Contact Key
  
  @objc(setContactKey:)
  func setContactKey(contactKey: String?) {
    Dengage.setContactKey(contactKey: contactKey)
  }
  
  @objc
  func getContactKey(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    resolve(Dengage.getContactKey())
  }
  
  // MARK: - Navigation
  
  @objc(setNavigation:)
  func setNavigation(screenName: NSString) {
    Dengage.setNavigation(screenName: screenName as String)
  }
  
  // MARK: - In-App Device Info
  
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
  
  // MARK: - Subscription
  
  @objc
  func getSubscription(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    let subscription = Subscription(
      integrationKey: "",
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
          
          let dict: [String: Any] = [
            "id": message.id,
            "title": message.title ?? "",
            "message": message.message ?? "",
            "mediaURL": message.mediaURL ?? "",
            "targetUrl": message.targetUrl ?? "",
            "receiveDate": message.receiveDate != nil ? formatter.string(from: message.receiveDate!) : "",
            "isClicked": message.isClicked,
            "carouselItems": carouselArr
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
        resolve(["success": true, "id": id])
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
        resolve(["success": true, "id": id])
        break;
      case .failure (let error):
        reject("error", error.localizedDescription , error)
        break;
      }
    }
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  @objc(multiply:withB:withResolver:withRejecter:)
  func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
    resolve(a*b)
  }
  
  @objc(setIntegrationKey:)
  func setIntegrationKey(key: String) -> Void {
    Dengage.setIntegrationKey(key: key)
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
  
  @objc(showRealTimeInApp:withData:)
  func showRealTimeInApp (_ screenName: NSString, withData: NSDictionary) -> Void {
    do {
      Dengage.showRealTimeInApp(screenName: screenName as String , params: withData as? Dictionary<String, String> )
    } catch {
      print("Unexpected search error: \(error)")
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
  
  
  // NEW
  
  @objc
  func getSdkVersion(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
    resolve(Dengage.getSdkVersion())
  }
}
