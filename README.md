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

const result = await Dengage.multiply(3, 7);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
