//
//  DengageCoordinatorHelper.m
//  ReactNewTest
//

#import "DengageCoordinatorHelper.h"
@import react_native_dengage;

@implementation DengageCoordinatorHelper


+ (void)registerDeviceToken:(NSData *)deviceToken {
  
  DengageRNCoordinator *coordinator = [DengageRNCoordinator staticInstance];
  [coordinator registerForPushToken:deviceToken];
  
}

+ (void)handleDengageIntialization:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  
  
  DengageRNCoordinator *coordinator = [DengageRNCoordinator staticInstance];
  [coordinator setValue:launchOptions forKey:@"launchOptions"];
  
  [coordinator setupDengage:@"hVt7KpAkwbJXRO_s_l_p6To_p_l_9lIaG3HyOp2pYtPwnpzML4D5AGhv88nXj4tdG1MJOsDk0rE072ewsGRGyxdt7V7UAEO_s_l_mN01MRl6iQDiCbx_s_l_ndwua1_s_l_5KL8MXzpLiGbjvFol" launchOptions:launchOptions application:application askNotificaionPermission:false];
  
  
}


+ (void)userNotification:(UNUserNotificationCenter *)center notificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler
{
  DengageRNCoordinator *coordinator = [DengageRNCoordinator staticInstance];
  [coordinator didReceivePush:center response:response withCompletionHandler:completionHandler];

}

@end
