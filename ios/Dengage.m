#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(DengageRN, NSObject)

- (NSArray<NSString *> *)supportedEvents
{
  return @[@"onNotificationClicked"];
}

RCT_EXTERN_METHOD(multiply:(float)a withB:(float)b
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setIntegerationKey:(NSString *)key)

RCT_EXTERN_METHOD(promptForPushNotifications)

RCT_EXTERN_METHOD(promptForPushNotificationsWitCallback:(RCTResponseSenderBlock)callback)

RCT_EXTERN_METHOD(setUserPermission:(BOOL)permission)

RCT_EXTERN_METHOD(registerForRemoteNotifications:(BOOL)enable)

RCT_EXTERN_METHOD(getToken:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getContactKey:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setToken:(NSString *)token)

RCT_EXTERN_METHOD(setLogStatus:(BOOL)isVisible)

RCT_EXTERN_METHOD(setContactKey:(NSString *)contactKey)

RCT_EXTERN_METHOD(handleNotificationActionBlock:(RCTResponseSenderBlock)callback)

RCT_EXTERN_METHOD(registerNotificationListeners)

RCT_EXTERN_METHOD(pageView:(NSDictionary *)data)

RCT_EXTERN_METHOD(addToCart:(NSDictionary *)data)

RCT_EXTERN_METHOD(removeFromCart:(NSDictionary *)data)

RCT_EXTERN_METHOD(viewCart:(NSDictionary *)data)

RCT_EXTERN_METHOD(beginCheckout:(NSDictionary *)data)

RCT_EXTERN_METHOD(placeOrder:(NSDictionary *)data)

RCT_EXTERN_METHOD(cancelOrder:(NSDictionary *)data)

RCT_EXTERN_METHOD(addToWishList:(NSDictionary *)data)

RCT_EXTERN_METHOD(removeFromWishList:(NSDictionary *)data)

RCT_EXTERN_METHOD(search:(NSDictionary *)data)

RCT_EXTERN_METHOD(sendDeviceEvent:(NSString *)tableName withData:(NSDictionary *)withData)

RCT_EXTERN_METHOD(getInboxMessages:(NSInteger *)offset limit:(NSInteger *)limit resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(deleteInboxMessage:(NSString *)id resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setInboxMessageAsClicked:(NSString *)id resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setNavigation)

RCT_EXTERN_METHOD(setNavigationWithName:(NSString *)screenName)

@end
