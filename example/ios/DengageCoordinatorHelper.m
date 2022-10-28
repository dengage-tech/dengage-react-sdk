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
  
  [coordinator setupDengage:@"BWUdMrMrI3YqDvJx_p_l_kkev8JcCFSHM5rkRCpwQdnvEWvMaRp7n_s_l_5olQe0RW_p_l_mI8BtHeFvsOBEYqR_s_l_YeZK6Cfr2DyN9nVJi2faUgyGgYdoeRHALe_p_l_ROuJxm0V5eBFKdZg9H7ULjDr4tU2Q0VJsgzqaRQ_e_q__e_q_" launchOptions:launchOptions application:application askNotificaionPermission:false];
  
  
}


+ (void)userNotification:(UNUserNotificationCenter *)center notificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler
{
  DengageRNCoordinator *coordinator = [DengageRNCoordinator staticInstance];
  [coordinator didReceivePush:center response:response withCompletionHandler:completionHandler];

}

@end
