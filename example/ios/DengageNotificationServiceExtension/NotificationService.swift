//
//  NotificationService.swift
//  DengageNotificationServiceExtension
//
//  Created by Kamran Younis on 15/03/2021.
//

import UserNotifications
//import Dengage

class NotificationService: UNNotificationServiceExtension {

    var contentHandler: ((UNNotificationContent) -> Void)?
    var bestAttemptContent: UNMutableNotificationContent?

//  override func didReceive(_ request: UNNotificationRequest, withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void) {
//    self.contentHandler = contentHandler
//    bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)
//
//    if let bestAttemptContent = bestAttemptContent {
//
//        // add this line of code
//        Dengage.didReceiveNotificationExtentionRequest(receivedRequest: request, with: bestAttemptContent)
//        contentHandler(bestAttemptContent)
//    }
//}
}
