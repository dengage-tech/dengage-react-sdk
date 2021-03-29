#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(DengageRN, NSObject)

RCT_EXTERN_METHOD(multiply:(float)a withB:(float)b
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setIntegerationKey:(NSString *)key)

RCT_EXTERN_METHOD(promptForPushNotifications)

RCT_EXTERN_METHOD(promptForPushNotificationsWitCallback:(RCTResponseSenderBlock)callback)

RCT_EXTERN_METHOD(setUserPermission:(BOOL)permission)

RCT_EXTERN_METHOD(getToken:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getContactKey:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setToken:(NSString *)token)

RCT_EXTERN_METHOD(setLogStatus:(BOOL)isVisible)

RCT_EXTERN_METHOD(setContactKey:(NSString *)contactKey)

RCT_EXTERN_METHOD(handleNotificationActionBlock:(RCTResponseSenderBlock)callback)

@end
