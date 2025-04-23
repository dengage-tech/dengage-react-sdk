#import "AppDelegate.h"

#import <React/RCTBundleURLProvider.h>
#import "DengageCoordinatorHelper.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  self.moduleName = @"ReactNativeDengageExample";
  // You can add your custom initial props in the dictionary below.
  // They will be passed down to the ViewController used by React Native.
  
  [DengageCoordinatorHelper handleDengageInitialization:application didFinishLaunchingWithOptions:launchOptions];

  
  self.initialProps = @{};

  return [super application:application didFinishLaunchingWithOptions:launchOptions];
}


// Implement required UNUserNotificationCenterDelegate methods

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
  [DengageCoordinatorHelper registerDeviceToken:deviceToken];

}

// Called when a notification is delivered to a foreground app
- (void)userNotificationCenter:(UNUserNotificationCenter *)center
       willPresentNotification:(UNNotification *)notification
         withCompletionHandler:(void (^)(UNNotificationPresentationOptions options))completionHandler {
  // Show the notification even when the app is in foreground
  completionHandler(UNNotificationPresentationOptionBadge | UNNotificationPresentationOptionSound | UNNotificationPresentationOptionBanner);
}

// Called when a user selects a notification or selects an action from a notification
- (void)userNotificationCenter:(UNUserNotificationCenter *)center
didReceiveNotificationResponse:(UNNotificationResponse *)response
         withCompletionHandler:(void(^)(void))completionHandler {
  // Handle the notification response
  // Let DengageCoordinatorHelper handle the notification if needed
  // [DengageCoordinatorHelper handleNotificationResponse:response];
  
  completionHandler();
}

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
  return [self bundleURL];
}


- (NSURL *)bundleURL
{
#if DEBUG
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
  return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

@end
