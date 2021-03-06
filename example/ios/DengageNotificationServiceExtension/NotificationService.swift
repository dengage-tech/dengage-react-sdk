//
//  NotificationService.swift
//  DengageNotificationServiceExtension
//
//  Created by Macbook Pro on 30/03/2021.
//

import UserNotifications
import Dengage_Framework

class NotificationService: UNNotificationServiceExtension {

    var contentHandler: ((UNNotificationContent) -> Void)?
    var bestAttemptContent: UNMutableNotificationContent?

      override func didReceive(_ request: UNNotificationRequest, withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void) {
          self.contentHandler = contentHandler
          bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)
          if let bestAttemptContent = bestAttemptContent {
            Dengage.didReceiveNotificationExtentionRequest(receivedRequest: request, withNotificationContent: bestAttemptContent)
            contentHandler(bestAttemptContent)
          }
      }
    override func serviceExtensionTimeWillExpire() {
        // Called just before the extension will be terminated by the system.
        // Use this as an opportunity to deliver your "best attempt" at modified content, otherwise the original push payload will be used.
        if let contentHandler = contentHandler, let bestAttemptContent =  bestAttemptContent {
            contentHandler(bestAttemptContent)
        }
    }
}
