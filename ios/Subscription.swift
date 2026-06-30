//
//  Subscription.swift
//  Pods
//
//  Created by Egemen Gülkılık on 23.04.2025.
//

import Foundation
import Dengage

public class Subscription: Codable {
    var integrationKey: String
    var token: String?
    var appVersion: String?
    var sdkVersion: String
    var deviceId: String?
    var advertisingId: String
    var carrierId: String
    var contactKey: String?
    var permission: Bool?
    var trackingPermission: Bool
    var tokenType: String
    var webSubscription: String?
    var testGroup: String
    var country: String?
    var language: String
    var timezone: String
    var partnerDeviceId: String?
    var locationPermission: String?
    
    init(
        integrationKey: String = "",
        token: String? = nil,
        appVersion: String? = "",
        sdkVersion: String = "",
        deviceId: String? = "",
        advertisingId: String = "",
        carrierId: String = "",
        contactKey: String? = "",
        permission: Bool? = true,
        trackingPermission: Bool = true,
        tokenType: String = "I",
        webSubscription: String? = nil,
        testGroup: String = "",
        country: String? = "",
        language: String = "",
        timezone: String = "",
        partnerDeviceId: String? = "",
        locationPermission: String? = ""
    ) {
        self.integrationKey = integrationKey
        self.token = token
        self.appVersion = appVersion
        self.sdkVersion = sdkVersion
        self.deviceId = deviceId
        self.advertisingId = advertisingId
        self.carrierId = carrierId
        self.contactKey = contactKey
        self.permission = permission
        self.trackingPermission = trackingPermission
        self.tokenType = tokenType
        self.webSubscription = webSubscription
        self.testGroup = testGroup
        self.country = country
        self.language = language
        self.timezone = timezone
        self.partnerDeviceId = partnerDeviceId
        self.locationPermission = locationPermission
    }
    
    static func currentFromDengage() -> Subscription {
        let appVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? ""
        let trackingPermission = DengageLocalStorage.shared.value(for: .trackingPermission) as? Bool ?? true
        let country = DengageLocalStorage.shared.value(for: .countrySubscription) as? String
        let language = DengageLocalStorage.shared.value(for: .languageSubscription) as? String
            ?? Locale.current.languageCode ?? ""
        let timezone = DengageLocalStorage.shared.value(for: .timezoneSubscription) as? String
            ?? TimeZone.current.identifier
        let partnerDeviceId = DengageLocalStorage.shared.value(for: .partner_device_idSubscription) as? String
        let carrierId = DengageLocalStorage.shared.value(for: .carrierIdSubscription) as? String ?? ""
        let locationPermission = DengageLocalStorage.shared.value(for: .locationPermissionSubscription) as? String
        let advertisingId = DengageLocalStorage.shared.value(for: .advertisingIdSubscription) as? String ?? ""

        return Subscription(
            integrationKey: Dengage.getIntegrationKey(),
            token: Dengage.getDeviceToken(),
            appVersion: appVersion,
            sdkVersion: Dengage.getSdkVersion() ?? "",
            deviceId: Dengage.getDeviceId() ?? "",
            advertisingId: advertisingId,
            carrierId: carrierId,
            contactKey: Dengage.getContactKey(),
            permission: Dengage.getPermission(),
            trackingPermission: trackingPermission,
            tokenType: "I",
            webSubscription: nil,
            testGroup: "",
            country: country ?? Locale.current.regionCode,
            language: language,
            timezone: timezone,
            partnerDeviceId: partnerDeviceId ?? "",
            locationPermission: locationPermission ?? ""
        )
    }

    func toDictionary() -> [String: Any] {
        return [
            "integrationKey": integrationKey,
            "token": token as Any,
            "appVersion": appVersion as Any,
            "sdkVersion": sdkVersion,
            "deviceId": deviceId as Any,
            "advertisingId": advertisingId,
            "carrierId": carrierId,
            "contactKey": contactKey as Any,
            "permission": permission as Any,
            "trackingPermission": trackingPermission,
            "tokenType": tokenType,
            "webSubscription": webSubscription as Any,
            "testGroup": testGroup,
            "country": country as Any,
            "language": language,
            "timezone": timezone,
            "partnerDeviceId": partnerDeviceId as Any,
            "locationPermission": locationPermission as Any
        ]
    }
}
