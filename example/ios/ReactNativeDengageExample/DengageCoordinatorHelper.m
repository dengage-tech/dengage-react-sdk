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
NSString *integrationKey = @"9MBNB5X2IWf8oBfxaNjs5kWcmObGwc8g6bmJcqS2rprtPSJgAThZL_s_l_n1nypZLOoQApPKMzfFMoJpU_s_l_BQk9YpJMk3mn05bpF3_p_l_1XjtNC1jvrEkEZ3D8h5VmUz0U4xmiI0ycs7_s_l_BJ20fOwTQsOq5OXRA_e_q__e_q_";

//dev-app.dengage.com: ios-rn-testflight-test
//NSString *integrationKey = @"n_p_l_lO9FMM4fzlX4jbudL355PGRpKsipGVHMgaPbOGAxR_s_l_MJo3J1KWkHYAz4u93U09KjPXZeVqZAZpC_s_l_JjJSlGPBWiHO2pIFsMG71SR5_p_l_YMNTnN1_p_l_Aje3MXqKCRpYjEUBNroiGTGF609W8EhEcRaQAZA_e_q__e_q_";


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
