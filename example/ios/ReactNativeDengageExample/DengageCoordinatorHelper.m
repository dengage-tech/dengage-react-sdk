//
//  DengageCoordinatorHelper.m
//  ReactNativeDengageExample
//
//  Created by Egemen Gülkılık on 9.01.2025.
//

#import "DengageCoordinatorHelper.h"
@import react_native_dengage;

@implementation DengageCoordinatorHelper


+ (void)registerDeviceToken:(NSData *)deviceToken {
  
  DengageRNCoordinator *coordinator = [DengageRNCoordinator staticInstance];
  [coordinator registerForPushToken:deviceToken];
  
}

+ (void)handleDengageInitialization:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  
  
  DengageRNCoordinator *coordinator = [DengageRNCoordinator staticInstance];
  [coordinator setValue:launchOptions forKey:@"launchOptions"];
  
  [coordinator setupDengage:@"RtWFz7e1SIvcYm3IeaPg6mWwtSZS_p_l_uZUZjQ5H5PYMphJDQaj_p_l_2x_p_l_sygPdd8k4OnwDfKfxy8e6zfLezdFrlmUAVDz1o2avZsolsEgQq3eDJy_p_l_TLRRjHT2wzGZwPkbmTla5_p_l_uM4299j2Jde4iNO9MLcA_e_q__e_q_" launchOptions:launchOptions application:application askNotificaionPermission:true enableGeoFence:true disableOpenURL:false badgeCountReset:false logVisible:true];
  
  
}


+ (void)userNotification:(UNUserNotificationCenter *)center notificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler
{
  DengageRNCoordinator *coordinator = [DengageRNCoordinator staticInstance];
  [coordinator didReceivePush:center response:response withCompletionHandler:completionHandler];

}

@end
