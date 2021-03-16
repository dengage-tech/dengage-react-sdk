# react-native-dengage

**dEngage Customer Driven Marketing Platform (CDMP)** serves as a customer data platform (CDP) with built-in omnichannel marketing features. It replaces your marketing automation and cross-channel campaign management.
The platform positions itself between our client’s data sources and its customers. It is also capable of exporting anonymous data to 3rd party networks such as **Oracle Bluekai, Criteo, Facebook Custom Audiences** and **Google Customer** Match to support retargeting and remarketing efforts of our clients.
For further details about dEngage please [visit here](https://docs.dengage.com).

This package makes it easy to integrate, dEngage, with your React-Native iOS and/or Android apps. Following are instructions for installation of react-native-dengage SDK to your react-native applications.

## Installation

```sh
npm install react-native-dengage
```

or using ```yarn```

```sh
yarn add react-native-dengage
```

## Linking

<details>
  <summary> iOS Linking </summary>
  
  #### React Native 0.60 and above
  Run npx ```pod-install```. Linking is not required in React Native 0.60 and above.
  
  #### React Native 0.59 and below
  Run ```react-native link react-native-dengage``` to link the react-native-dengage library.

</details>

<details>
  <summary> android Linking </summary>
  
  Linking is NOT required in React Native 0.60 and above. If your project is using React Native < 0.60, run ```react-native link react-native-dengage``` to link the react-native-dengage library.

Or if you have trouble, make the following additions to the given files manually:

#### android/settings.gradle

```
include ':react-native-dengage'
project(':react-native-dengage').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-dengage/android')
```

#### android/app/build.gradle

```
dependencies {
   ...
   implementation project(':react-native-dengage')
}
```

#### MainApplication.java

On top, where imports are:

```
import com.reactnativedengage.DengagePackage;
```

Add the DengagePackage class to your list of exported packages.

```
@Override
protected List<ReactPackage> getPackages() {
    return Arrays.asList(
            new MainReactPackage(),
            new DengagePackage()
    );
}
```

</details>

## Platform Specific Extra Steps
Following extra steps after the installation of the react-native-dengage SDK are required for it to work properly.

<details>
  <summary> iOS Specific Extra steps </summary>
  
  #### Requirements
  - dEngage Integration Key
  - iOS Push Cerificate
  - iOS Device (you need to test on a real device for notifications)
  - A mac with latest Xcode

  ### Steps
  
  #### 1. Endpoint Configuration in PInfo.list
  For initial setup, if you have given URL addresses by dEngage Support team, you need to setup url address by using ```Info.plist``` file. Otherwise you don’t need to add anything to ```Info.plist``` file. Following screenshot for the keys in ```Info.plist``` file.
  
  ![Info.plist screenshot](https://raw.githubusercontent.com/whitehorse-technology/Dengage.Framework/master/docs/img/Screen%20Shot%202020-09-25%20at%2015.41.27.png)

> Note: Please see API Endpoints by Datacenter documentation in this section for end points. [here is link](https://dev.dengage.com/mobile-sdk/api-endpoints)

  #### 2. Add Required Capabilities
  In Xcode, select the root project and main app target. In ***Signing & Capabilities***, select ***All*** and ***+ Capability***. Add "Push Notifications" and ***Background Modes***
  <summary> screenshot 1 </summary>

  ![push notifications](https://files.readme.io/17798af-dengage_push_Step1.png)
  
  <summary> screenshot 2 </summary>
  
  ![background modes](https://files.readme.io/badc90e-dengage_push_step2.png)

  #### 3. Add Notification Service Extension (required only if using rich notifications)
  The ```DengageNotificationServiceExtension``` allows your application to receive rich notifications with images and/or buttons, and to report analytics about which notifications users receive.
  3.1 In Xcode Select ```File``` > ```New``` > ```Target```
  3.2 Select ```Notification Service Extension``` then press ```Next```
  
  ![step 3.2 screenshot](https://github.com/whitehorse-technology/Dengage.Framework/raw/master/docs/img/extension.png)
  
  3.3 Enter the product name as ```DengageNotificationServiceExtension``` and press ```Finish```.
  
  > Do NOT press "Activate" on the dialog shown after this.
  
  ![step 3.3 screenshot](https://github.com/whitehorse-technology/Dengage.Framework/raw/master/docs/img/settings.png)
  
  3.4 Press ```Cancel``` on the Activate scheme prompt.
  
  ![step 3.4 screenshot](https://github.com/whitehorse-technology/Dengage.Framework/raw/master/docs/img/activate.png)

  > By canceling, you are keeping Xcode debugging your app, instead of just the extension. If you activate by accident, you can always switch back to debug your app within Xcode (next to the play button).
  
  3.5 In the ***Project Navigator***, select the top-level project directory and select the ```DengageNotificationServiceExtension``` target in the ***project and targets list***. Ensure the ```Deployment Target``` is set to ```iOS 10``` for maximum platform compatibility.
  
  ![step 3_5 screenshot](https://files.readme.io/c834169-step3_5_iOS_version.png)

  3.6 Finish Notification Service Extension Setup
  
  If you did not use ***Cocoapods***, follow [these steps](google.com).
  
  > Note: non-cocoapods steps yet to be determined.
  
  Otherwise, continue with the following setup:
  - In your ```Project Root``` > ```iOS``` > ```Podfile```, add the notification service extension outside the main target (should be at the same level as your main target):
  
  ```Ruby
    target 'DengageNotificationServiceExtension' do
      pod 'Dengage.Framework',‘~> 2.5’
    end
  ```
  
  Close Xcode. While still in the ```ios``` directory, run ```pod install``` again.
  
  - Open the ```.xcworkspace``` file in Xcode. In the ```DengageNotificationServiceExtension directory``` > ```NotificationService.swift``` file, replace the whole file contents with the code below:
  
  ```Swift
      override func didReceive(_ request: UNNotificationRequest, withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void) {
        self.contentHandler = contentHandler
        bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)
         
        if let bestAttemptContent = bestAttemptContent {
            
            // add this line of code
            Dengage.didReceiveNotificationExtentionRequest(receivedRequest: request, with: bestAttemptContent)
            contentHandler(bestAttemptContent)
        }
    }
  ```
  > Ignore any build errors at this point, we will resolve these later by importing the Dengage library.
  
  ![NotificationService.swift screenshot](https://files.readme.io/56a0fd9-3_6_NotificationService_screenshot.png)

  ### 4. Setting Integration Key
  ***Integration Key*** is genereted by CDMP Platform while defining application. It is a hash string which contains information about application.
  At the beginning of your application cycle you must set Integration Key.
  To set integration key SDK Provides ```Dengage.setIntegrationKey(key: String)``` method.
  Call this method at the begining of your application life cycle.
  
  > Recommendation : Use it on AppDelegate.m
  
  ```Swift
    Dengage.setIntegrationKey(key: String)
  ```
  
  ### 5. Initialization with Launch Options
  After setting Integration Key, to use SDK features, ```Dengage.initWithLaunchOptions(withLaunchOptions: [UIApplication.LaunchOptionsKey: Any]?,badgeCountReset: Bool?)``` function must be called.
  
***Parameters***
  ```withLaunchOptions```: ```[UIApplication.LaunchOptionsKey: Any]?```: Pass didFinishLaunchingWithOptions params
  ```badgeCountReset```: ```bool``` If you want to reset (clear) badge icon on your notifications set this option to true

***Sample:***
In the ```AppDelegate.m```
```
import Dengage_Framework

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, REFrostedViewControllerDelegate {
    // STEP 1: setting integration key
    let integrationKey = "Your-Integeration-Key-Here"
    Dengage.setIntegrationKey(key: integrationKey)

    // STEP 2: setting initWithLauchOptions
    Dengage.initWithLaunchOptions(withLaunchOptions: launchOptions, badgeCountReset: false)
}
```
  
</details>

<details>
  <summary> android Specific Extra Steps </summary>
  
</details>

## Supported Versions 
<details>
  <summary> iOS </summary>
  
  dEngage Mobile SDK for IOS supports version IOS 10 and later.
</details>

<details>
  <summary> android </summary>
  
  dEngage Mobile SDK for Android supports version 4.4 (API Level 19) and later.

  <summary> Huawei </summary>
  
  dEngage Mobile SDK for Huawei supports all new versions.
</details>


## Usage

```js
import Dengage from "react-native-dengage";

// ...

Dengage.setIntegrationKey("Your-Dengage-Integeration-Key");
```

### Subscription
****Subscription is a process which is triggered by sending subscription event to Dengage. It contains necessary informations about application to send push notifications to clients.****

Subscriptions are self managed by dEngage SDK and subcription cycle starts with Prompting user permission. SDK will automaticlly send subscription events under following circumstances:

- Setting Contact key
- Setting Token
- Setting User Permission (if you have manual management of permission)

### Asking User Permission for Notification
> Note: Android doesn't require to ask for push notifications explicitely. Therefore, you can only ask for push notification's permissions on iOS.

IOS uses shared `UNUserNotificationCenter` by itself while asking user to send notification. Dengage SDK manager uses `UNUserNotificationCenter` to ask permission as well. [Apple Doc Reference](https://developer.apple.com/documentation/usernotifications/asking_permission_to_use_notifications)

If in your application, you want to get UserNotification permissions explicitely, you can do by calling one of the following methods:

```Javascript
// At the top import
import dEngage from 'react-native-dengage'

// somewhere in your javascript/typescript code
dEngage.promptForPushNotifications()
```

OR

```Javascript
// At the top import
import dEngage from 'react-native-dengage'

// somewhere in your javascript/typescript code
dEngage.promptForPushNotifications((hasPermission: Boolean) => {
  // do somthing with hasPermission flag.
  // Note: hasPermission provides information if user enabled or disabled notification permission from iOS Settings > Notifications.
  
  // here you may send application's permission state using 
  dEngage.setUserPermission(hasPermission)
})
```
### User Permission Management (optional)
If you manage your own user permission states on your application you may send user permission by using `setUserPermission` method.
```Javascript
// At the top import
import dEngage from 'react-native-dengage'

// somewhere in your javascript/typescript code
dEngage.setUserPermission(true)
```

### Getting Mobile Push Token

```Javascript
// At the top import
import dEngage from 'react-native-dengage'

// somewhere in your javascript/typescript code
const token = await dEngage.getToken()
```


### Setting Mobile Push Token

```Javascript
// At the top import
import dEngage from 'react-native-dengage'

// somewhere in your javascript/typescript code
dEngage.setToken(token)
```

### Setting Contact Key
***Contact Key represents a value which has a relation with Devices and Contacts. There are two types of devices. Anonymous Devices and Contact Devices. Contact Devices contains Contact Key.***

To track devices by their contacts you need to set contact key on SDK.

> Note: It is recommended to call this method, if you have user information. You should call in every app open and on login, logout pages.

```
// import statement
import dEngage from 'react-native-dengage'

// in js/ts code
dEngage.setContactKey(contactKey)
```

### Logging
SDK can provide logs for debuging. It displays queries and payloads which are sent to REST API’s.

To validate your inputs you can enable SDK’s log by a method.

```
// isVisible is Boolean. By default isVisible set to false.
dEngage.setLogStatus(isVisible)
```

### Handling Notification Action Callback
SDK provides a method if you want to get and parse payload manually for custom parameters or etc.

```
dEngage.handleNotificationActionBlock((notificationResponse: Object) => {
  // handle notification Response here.
})
```

### DeepLinking
SDK supports URL schema deeplink. If target url has a valid link, it will redirect to the related link.
Please see related links below about deeplinking.
<details>
  <summary> iOS Specific Links </summary>
  
  [Apple Url Scheme Links](https://developer.apple.com/documentation/xcode/allowing_apps_and_websites_to_link_to_your_content/defining_a_custom_url_scheme_for_your_app)
  
  [Apple Universal Link](https://developer.apple.com/documentation/xcode/allowing_apps_and_websites_to_link_to_your_content)
</details>

<details>
  <summary> android Specific Links </summary>
  
  [Create a deep link for a destination](https://developer.android.com/guide/navigation/navigation-deep-link)
  
  [Create Deep Links to App Content](https://developer.android.com/training/app-links/deep-linking)
</details>

### Rich Notifications <a name="rich_push" />
Rich Notifications is a notification type which supports image, gif, video content. Dengage SDK supports varieties of contents and handles notification.
Rich Notifications supports following media types:
- Image
- Video
- Gif

For further details about rich notification and its setup on iOS side please follow [this link](https://dev.dengage.com/mobile-sdk/ios/richnotification)

> Note: on Android there is no special setup required for rich notifications.

### Carousel Push <a name="carousel_push" />
Carousel Push is a notification type which has a different UI than Rich Notification. SDK will handle notification payload and displays UI if it’s a carousel push. Carousel Push functionality allows you to show your notification with a slideshow.

<details>
  <summary> iOS </summary>
  
  ### Requirements
  - iOS 10 or higher
  - Notification Service Extension
  - Notification Content Extension
  - Dengage.Framework.Extensions

  to setup Carousel Push on iOS you can follow [this link](https://dev.dengage.com/mobile-sdk/ios/carousel-push)
</details>

<details>
  <summary> android </summary>
  
  ### Requirements
  - Android SDK 2.0.0+
  
  to setup Carousel Push on android you can follow [this link](https://dev.dengage.com/mobile-sdk/android/carousel-push)
</details>

### Action Buttons <a name="action_buttons" />
Android SDK allows you to put clickable buttons under the notification. Action buttons are supported in Android SDK 2.0.0+.
For further setup of Action Buttons, follow [this link](https://dev.dengage.com/mobile-sdk/android/action-buttons).

### Event Collection <a name="event_collection" />
In order to collect android mobile events and use that data to create behavioral segments in dEngage you have to determine the type of events and data that needs to collected. Once you have determined that, you will need to create a “Big Data” table in dEngage. Collected events will be stored in this table. Multiple tables can be defined depending on your specific need.

Any type of event can be collected. The content and the structure of the events are completely flexible and can be changed according to unique business requirements. You will just need to define a table for events.

Once defined, all you have to do is to send the event data to these tables. D·engage SDK has only two functions for sending events: `sendDeviceEvent` and `sendCustomEvent`. Most of the time you will just need the `sendDeviceEvent` function.

### 1. Login / Logout Action
If the user loggs in or you have user information, this means you have contact_key for that user. You can set contact_key in order to match user with the browser. There are two functions for getting and setting contact_key.

### 1.a setContactKey
If user logged in set user id. This is important for identifying your users. You can put this function call in every page. It will not send unnecessary events.
[code example is here](#setting-contact-key)

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
