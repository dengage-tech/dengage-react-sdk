import Dengage_Framework

@objc(DengageRN)
class DengageRN: NSObject {

    @objc(multiply:withB:withResolver:withRejecter:)
    func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        resolve(a*b)
    }
    
    @objc(setIntegerationKey:)
    func setIntegerationKey(_ key: String) -> Void {
        Dengage.setIntegrationKey(key: key)
    }

    @objc
    func promptForPushNotifications() {
        Dengage.promptForPushNotifications()
    }
    
    
    
}
