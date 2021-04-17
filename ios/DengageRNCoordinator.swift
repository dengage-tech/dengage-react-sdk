//
//  DengageRNCoordinator.swift
//  react-native-dengage
//
//  Created by Kamran Younis on 22/03/2021.
//

import Foundation
import React
import Dengage_Framework

@objc(DengageRNCoordinator)
public class DengageRNCoordinator: NSObject {
    @objc public static let staticInstance: DengageRNCoordinator = DengageRNCoordinator()

    // todo: will remove in case not used.
    @objc var integerationKey: String?
    @objc var launchOptions: [UIApplication.LaunchOptionsKey: Any]?

    @objc(setupDengage:launchOptions:)
    public func setupDengage(key:NSString, launchOptions:NSDictionary?) {
        Dengage.setIntegrationKey(key: key as String)
        if (launchOptions != nil) {
            Dengage.initWithLaunchOptions(withLaunchOptions: launchOptions as! [UIApplication.LaunchOptionsKey : Any])
        } else {
            Dengage.initWithLaunchOptions(withLaunchOptions: nil)
        }
        Dengage.promptForPushNotifications()
    }
    
    @objc(registerForPushToken:)
    public func registerForPushToken(deviceToken: Data) {
        var token = "";
        if #available(iOS 13.0, *){
            token = deviceToken.map { String(format: "%02x", $0) }.joined()
        }
        else {
            let tokenParts = deviceToken.map { data in String(format: "%02.2hhx", data) }
            token = tokenParts.joined()
        }
        sendToken(token)
    }
    
    private func sendToken(_ token: String ){
        Dengage.setToken(token: token)
    }
}
