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
}
