#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(DengageRN, NSObject)

RCT_EXTERN_METHOD(multiply:(float)a withB:(float)b
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setIntegerationKey:(NSString *)key)

RCT_EXTERN_METHOD(promptForPushNotifications)

RCT_EXTERN_METHOD(promptForPushNotifications)

@end
