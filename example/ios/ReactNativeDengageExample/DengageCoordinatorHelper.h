//
//  DengageCoordinatorHelper.h
//  ReactNativeDengageExample
//
//  Created by Egemen Gülkılık on 9.01.2025.
//

#import <Foundation/Foundation.h>
#import <UserNotifications/UserNotifications.h>
#import "AppDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface DengageCoordinatorHelper : NSObject

+ (void)registerDeviceToken:(NSData *)deviceToken;
+ (void)handleDengageInitialization:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions;
+ (void)userNotification:(UNUserNotificationCenter *)center notificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler;

@end

NS_ASSUME_NONNULL_END
