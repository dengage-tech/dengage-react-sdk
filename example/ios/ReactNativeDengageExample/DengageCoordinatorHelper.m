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
NSString *integrationKey = @"n_p_l_lO9FMM4fzlX4jbudL355PGRpKsipGVHMgaPbOGAxR_s_l_MJo3J1KWkHYAz4u93U09KjPXZeVqZAZpC_s_l_JjJSlGPBWiHO2pIFsMG71SR5_p_l_YMNTnN1_p_l_Aje3MXqKCRpYjEUBNroiGTGF609W8EhEcRaQAZA_e_q__e_q_";


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

+ (void)userNotification:(UNUserNotificationCenter *)center
    notificationResponse:(UNNotificationResponse *)response
   withCompletionHandler:(void (^)(void))completionHandler
{
    [[DengageRNCoordinator staticInstance] didReceivePush:center response:response withCompletionHandler:completionHandler];
}

@end
