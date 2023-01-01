import Dengage

@objc(DengageRN)
class DengageRN: RCTEventEmitter {

    @objc(multiply:withB:withResolver:withRejecter:)
    func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve(a*b)
    }

    @objc(setIntegerationKey:)
    func setIntegerationKey(key: String) -> Void {
        Dengage.setIntegrationKey(key: key)
    }

    @objc
    func promptForPushNotifications() {
        Dengage.promptForPushNotifications()
    }

    @objc(promptForPushNotificationsWitCallback:)
    func promptForPushNotifications(callback: @escaping RCTResponseSenderBlock) {
        Dengage.promptForPushNotifications() { hasPermission in
            callback([hasPermission])
        }
    }

    @objc(setUserPermission:)
    func setUserPermission(permission: Bool) {
        Dengage.setUserPermission(permission: permission)
    }

    @objc(registerForRemoteNotifications:)
    func registerForRemoteNotifications(enable: Bool) {
        Dengage.set(permission: true)
       // Dengage.promptForPushNotifications()
    }

    // _ before resolve here is neccessary, need to revisit rn docs about why.
    @objc
    func getToken(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        do {
            let currentToken = try Dengage.getDeviceToken()
            resolve(currentToken)
        } catch {
            print("Unexpected getTOken error: \(error)")
            reject("UNABLE_TO_RETREIVE_TOKEN", error.localizedDescription ?? "Something went wrong", error)
        }
    }

    @objc
    func getContactKey(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        do {
            let contactKey = try Dengage.getContactKey()
            resolve(contactKey)
        } catch {
            print("Unexpected getContactKey error: \(error)")
            reject("UNABLE_TO_RETREIVE_CONTACT_KEY", error.localizedDescription ?? "Something went wrong", error)
        }
    }

    @objc(setToken:)
    func setToken(token: String) {
        Dengage.setToken(token: token)
    }

    @objc(setLogStatus:)
    func setLogStatus(isVisible: Bool) {
        Dengage.setLogStatus(isVisible: isVisible)
    }

    @objc(setContactKey:)
    func setContactKey(contactKey: String) {
        Dengage.setContactKey(contactKey: contactKey)
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
            for attachement in notificationResponse.notification.request.content.attachments {
                var contentAttachment = [String:Any?]()
                contentAttachment["identifier"] = attachement.identifier
                contentAttachment["url"] = attachement.url
                contentAttachment["type"] = attachement.type
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
            for attachement in notificationResponse.notification.request.content.attachments {
                var contentAttachment = [String:Any?]()
                contentAttachment["identifier"] = attachement.identifier
                contentAttachment["url"] = attachement.url
                contentAttachment["type"] = attachement.type
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

    @objc(getSubscription:withReject:)
    func getSubscription(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock){
        // this method is yet not available in iOS
        reject("NO_NATIVE_METHOD_YET", "this method is yet not available in iOS", nil)

//        do {
//            let contactId = try Dengage.getContactKey()
//            resolve(contactId)
//        } catch {
//            reject("UNABLE_TO_RETREIVE_CONTACT_KEY", error.localizedDescription ?? "Something went wrong", error)
//        }
    }

    @objc(getInboxMessages:limit:resolve:reject:)
       func getInboxMessages(offset: Int = 10, limit: Int = 20, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock){
           Dengage.getInboxMessages(offset: offset, limit: limit) { (result) in
               switch result {
                   case .success(let resultType): // do something with the result
                       do {
                           var arrDict = [[String:Any]]()
                           var arrCarousel = [[String:String]]()

                           let formatter = DateFormatter()
                           formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                           formatter.timeZone = TimeZone(abbreviation: "UTC")


                           for dict in resultType
                           {
                               if let items = dict.carouselItems
                               {
                                   for carousel in items
                                   {
                                       arrCarousel.append(["id": carousel.id ?? "", "title":carousel.title ?? "" , "descriptionText":carousel.descriptionText ?? "" , "mediaUrl": carousel.mediaUrl ?? "" ,  "targetUrl":carousel.targetUrl ?? ""])


                                   }

                                   arrDict.append(["message_json" : ["iosMediaUrl": dict.mediaURL ?? "", "iosTargetUrl":dict.targetUrl ?? "" , "iosCarouselContent": arrCarousel, "mediaUrl":dict.mediaURL ?? "" , "message": dict.message ?? "" , "receiveDate": formatter.string(from: dict.receiveDate ?? Date()) ?? "", "targetUrl":dict.targetUrl ?? "" , "title": dict.title ?? "" ], "is_clicked": dict.isClicked, "smsg_id": dict.id])
                               }
                               else
                               {
                                   arrDict.append(["message_json" : ["iosMediaUrl": dict.mediaURL ?? "", "iosTargetUrl":dict.targetUrl ?? "" , "iosCarouselContent": [], "mediaUrl":dict.mediaURL ?? "" , "message": dict.message ?? "" , "receiveDate": formatter.string(from: dict.receiveDate ?? Date()) ?? "", "targetUrl":dict.targetUrl ?? "" , "title": dict.title ?? "" ], "is_clicked": dict.isClicked, "smsg_id": dict.id])
                               }



                           }

                           let encodedData = try JSONSerialization.data(withJSONObject: arrDict, options: .prettyPrinted)

                           let jsonString = String(data: encodedData,
                                                   encoding: .utf8)
                           print("JSON String of inbox API \(jsonString)")
                           
                           resolve(jsonString)
                       } catch {
                           reject("error", error.localizedDescription , error)
                       }
                       break;
                   case .failure(let error): // Handle the error
                       reject("error", error.localizedDescription , error)
                       break;
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

    @objc(setNavigation)
    func setNavigation(){
        Dengage.setNavigation()
    }

    @objc(setNavigationWithName:)
    func setNavigationWithName(screenName: NSString) {
        Dengage.setNavigation(screenName: screenName as String)
    }
    
    @objc(stopGeofence)
    func stopGeofence(){
        Dengage.stopGeofence()
    }
    
    @objc(requestLocationPermissions)
    func requestLocationPermissions(){
        Dengage.requestLocationPermissions()
    }
    
    @objc(startGeofence)
    func startGeofence(){
        Dengage.requestLocationPermissions()
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
}
