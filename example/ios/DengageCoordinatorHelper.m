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
  
  [coordinator setupDengage:@"_p_l_NO5ikc0BTAhM9_s_l_N_p_l_Yuoww3Qy_p_l_Eh_s_l_hjBKP4axDG823EokwOHnQ6oNHTAubaZY7Bp1Pd_s_l_uCtuhzno_p_l_MuCxMHI9Hn3jANu9l2QzI3ISlSgnmqZtv1p0hDI8Sd5OaGoB1Dp3sHJu2tQQzHLREp2kdBCRA_e_q__e_q_" launchOptions:launchOptions application:application askNotificaionPermission:false disableOpenURL:false badgeCountReset:true];
  
  
}


+ (void)userNotification:(UNUserNotificationCenter *)center notificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler
{
  DengageRNCoordinator *coordinator = [DengageRNCoordinator staticInstance];
  [coordinator didReceivePush:center response:response withCompletionHandler:completionHandler];

}

@end
