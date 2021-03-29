import Dengage_Framework

@objc(DengageRN)
class DengageRN: NSObject {

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
    
    // _ before resolve here is neccessary, need to revisit rn docs about why.
    @objc
    func getToken(_ resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) {
        do {
            let currentToken = try Dengage.getToken()
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
            print()
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

            /**
             *notification response type
             *
             {
                actionIdentifier: String { get },
                notification: UNNotification {
                    date: Date { get }
                    request: UNNotificationRequest {
                     // The unique identifier for this notification request. It can be used to replace or remove a pending notification request or a delivered notification.
                     open var identifier: String { get }

                     
                     // The content that will be shown on the notification.
                     @NSCopying open var content: UNNotificationContent {
                            // Optional array of attachments.
                             open var attachments: [UNNotificationAttachment] {
                                 // The identifier of this attachment
                                 open var identifier: String { get }

                                 
                                 // The URL to the attachment's data. If you have obtained this attachment from UNUserNotificationCenter then the URL will be security-scoped.
                                 open var url: URL { get }

                                 
                                 // The UTI of the attachment.
                                 open var type: String { get }

                                 
                                 // Creates an attachment for the data at URL with an optional options dictionary. URL must be a file URL. Returns nil if the data at URL is not supported.
                                 public convenience init(identifier: String, url URL: URL, options: [AnyHashable : Any]? = nil) throws

                             }

                             
                             // The application badge number.
                             @NSCopying open var badge: NSNumber? { get }

                             
                             // The body of the notification.
                             open var body: String { get }

                             
                             // The identifier for a registered UNNotificationCategory that will be used to determine the appropriate actions to display for the notification.
                             open var categoryIdentifier: String { get }

                             
                             // The launch image that will be used when the app is opened from the notification.
                             open var launchImageName: String { get }

                             
                             // The sound that will be played for the notification.
                             @NSCopying open var sound: UNNotificationSound? { get }

                             
                             // The subtitle of the notification.
                             open var subtitle: String { get }

                             
                             // The unique identifier for the thread or conversation related to this notification request. It will be used to visually group notifications together.
                             open var threadIdentifier: String { get }

                             
                             // The title of the notification.
                             open var title: String { get }

                             
                             // Apps can set the userInfo for locally scheduled notification requests. The contents of the push payload will be set as the userInfo for remote notifications.
                             open var userInfo: [AnyHashable : Any] { get }

                             
                             /// The argument to be inserted in the summary for this notification.
                             @available(iOS 12.0, *)
                             open var summaryArgument: String { get }

                             
                             /// A number that indicates how many items in the summary are represented in the summary.
                             /// For example if a podcast app sends one notification for 3 new episodes in a show,
                             /// the argument should be the name of the show and the count should be 3.
                             /// Default is 1 and cannot be 0.
                             @available(iOS 12.0, *)
                             open var summaryArgumentCount: Int { get }

                             
                             // An identifier for the content of the notification used by the system to customize the scene to be activated when tapping on a notification.
                             @available(iOS 13.0, *)
                             open var targetContentIdentifier: String? { get } // default nil
                     }

                     
                     // The trigger that will or did cause the notification to be delivered. A nil trigger means deliver immediately.
                     @NSCopying open var trigger: UNNotificationTrigger? {
                        open var repeats: Bool { get }
                     }

                     
                     // Use a nil trigger to deliver immediately.
                     public convenience init(identifier: String, content: UNNotificationContent, trigger: UNNotificationTrigger?)

                    }
                }
             }
             */

        }
    }
}
