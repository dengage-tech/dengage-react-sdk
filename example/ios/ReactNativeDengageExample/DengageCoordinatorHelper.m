//
//  DengageCoordinatorHelper.m
//  ReactNativeDengageExample
//
//  Created by Egemen Gülkılık on 9.01.2025.
//

#import "DengageCoordinatorHelper.h"
@import react_native_dengage;

@implementation DengageCoordinatorHelper

//dev-app.dengage.com: ios-rn-sandbox-test
//NSString *integrationKey = @"RtWFz7e1SIvcYm3IeaPg6mWwtSZS_p_l_uZUZjQ5H5PYMphJDQaj_p_l_2x_p_l_sygPdd8k4OnwDfKfxy8e6zfLezdFrlmUAVDz1o2avZsolsEgQq3eDJy_p_l_TLRRjHT2wzGZwPkbmTla5_p_l_uM4299j2Jde4iNO9MLcA_e_q__e_q_";

//dev-app.dengage.com: ios-rn-testflight-test
NSString *integrationKey = @"PGvPIYn2stDRd8gMep_s_l_c086KUGXesU_p_l_akV3JOXcuFnvvvwUFJ_p_l_aVEPx8I41jjwlY4oJlMh5f6bbwv5CYVa3E0IbbvewUsLv8aklBeuPd1bQRiFj5sTL3HdMSaAlh9BcGgEUzERgt1WJPPZT7wbYVAw_e_q__e_q_";


+ (void)registerDeviceToken:(NSData *)deviceToken {
    [[DengageRNCoordinator staticInstance] registerForPushToken:deviceToken];
}

+ (void)handleDengageInitialization:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    DengageRNCoordinator *coordinator = [DengageRNCoordinator staticInstance];
    [coordinator setValue:launchOptions forKey:@"launchOptions"];
    [coordinator setupDengage:integrationKey
                 appGroupsKey:@"group.com.dengage.RNExample.dengage"
                launchOptions:launchOptions
                  application:application
    askNotificationPermission:YES
               enableGeoFence:YES
               disableOpenURL:NO
              badgeCountReset:NO
                   logVisible:YES];
}

+ (void)userNotificationCenter:(UNUserNotificationCenter *)center
didReceiveNotificationResponse:(UNNotificationResponse *)response
         withCompletionHandler:(void (^)(void))completionHandler
{
    [[DengageRNCoordinator staticInstance] didReceivePush:center response:response withCompletionHandler:completionHandler];
}


@end
