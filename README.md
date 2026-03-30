# Dengage React Native SDK — Integration Guide

This guide covers installing and configuring **@dengage-tech/react-native-dengage**, native Android and iOS setup, push (including rich and carousel), in-app messaging, inbox, events, and the full JavaScript API.

**Latest package version:** `2.0.8` (check [npm](https://www.npmjs.com/package/@dengage-tech/react-native-dengage) for updates).

---

## Table of contents

1. [Prerequisites](#1-prerequisites)
2. [Adding the SDK](#2-adding-the-sdk)
   - [2.1 Install from npm](#21-install-from-npm)
   - [2.2 iOS pods](#22-ios-pods)
   - [2.3 Autolinking](#23-autolinking)
   - [2.4 Local path (development)](#24-local-path-development)
3. [Backend URL configuration](#3-backend-url-configuration)
   - [3.1 Android — `AndroidManifest.xml`](#31-android--androidmanifestxml)
   - [3.2 iOS — `Info.plist`](#32-ios--infoplist)
4. [Android integration](#4-android-integration)
   - [4.1 Repositories and Firebase](#41-repositories-and-firebase)
   - [4.2 Native SDK version](#42-native-sdk-version)
   - [4.3 Geofence (optional)](#43-geofence-optional)
   - [4.4 `AndroidManifest.xml`](#44-androidmanifestxml)
   - [4.5 Initialize Dengage in `Application`](#45-initialize-dengage-in-application)
5. [iOS integration](#5-ios-integration)
   - [5.1 Pod dependency](#51-pod-dependency)
   - [5.2 Capabilities and background](#52-capabilities-and-background)
   - [5.3 `Info.plist` endpoints](#53-infoplist-endpoints)
   - [5.4 Initialize with `DengageRNCoordinator`](#54-initialize-with-dengagernicoordinator)
   - [5.5 App Groups](#55-app-groups)
6. [Rich push and carousel push](#6-rich-push-and-carousel-push)
   - [6.1 Rich push (Android)](#61-rich-push-android)
   - [6.2 Rich push (iOS) — Notification Service Extension](#62-rich-push-ios--notification-service-extension)
   - [6.3 Carousel (Android) — detailed guide](#63-carousel-android--detailed-guide)
   - [6.4 Carousel (iOS) — Notification Content Extension (detailed)](#64-carousel-ios--notification-content-extension-detailed)
7. [JavaScript / TypeScript API](#7-javascript--typescript-api)
   - [7.1 Identity, device, logging](#71-identity-device-logging)
   - [7.2 Push and permission](#72-push-and-permission)
   - [7.3 Commerce and analytics events](#73-commerce-and-analytics-events)
   - [7.4 In-app navigation and real-time in-app](#74-in-app-navigation-and-real-time-in-app)
   - [7.5 Cart object API](#75-cart-object-api)
   - [7.6 Inbox](#76-inbox)
   - [7.7 Geofence](#77-geofence)
   - [7.8 iOS notification actions](#78-ios-notification-actions)
8. [In-app inline and App Story](#8-in-app-inline-and-app-story)
   - [8.1 In-app inline](#81-in-app-inline)
   - [8.2 App Story](#82-app-story)
9. [Events, inbox, deep links](#9-events-inbox-deep-links)
   - [9.1 `NativeEventEmitter` (JavaScript)](#91-nativeeventemitter-javascript)
   - [9.2 Inbox behavior](#92-inbox-behavior)
   - [9.3 Deep links](#93-deep-links)
10. [Example app and troubleshooting](#10-example-app-and-troubleshooting)
    - [10.1 Run the bundled example](#101-run-the-bundled-example)
    - [10.2 Common issues](#102-common-issues)
    - [10.3 Example app folders (reference)](#103-example-app-folders-reference)
11. [Subscription model](#subscription-model)
12. [License](#license)
13. [Sample app on GitHub](#sample-app-on-github)

---

## 1. Prerequisites

- **React Native** `>= 0.65.0` and **React** `>= 17.0.2` (see `peerDependencies` in the package).
- **Android:** Android Studio, JDK, Firebase project with `google-services.json`, and an integration key from the Dengage dashboard.
- **iOS:** Xcode, CocoaPods, Apple Push Notification setup (key or certificate), and an iOS integration key from the Dengage dashboard.
- **Optional — Huawei (HMS):** Huawei developer setup, `agconnect-services.json`, and HMS dependencies if you target Huawei devices.
- **Optional — Geofence:** Location permissions and extra native dependencies (see [§4.3 Geofence (optional)](#43-geofence-optional)).


---

## 2. Adding the SDK

### 2.1 Install from npm

```bash
npm install @dengage-tech/react-native-dengage
# or
yarn add @dengage-tech/react-native-dengage
```

### 2.2 iOS pods

From your app’s `ios` directory:

```bash
cd ios && pod install && cd ..
```

**Optional geofence (iOS):** before `pod install`, enable the geofence pod dependency:

```bash
export install_dengage_geofence=1
pod install
```

### 2.3 Autolinking

React Native autolinking picks up the package. Rebuild the app after installing (`npx react-native run-android` / `run-ios`).

### 2.4 Local path (development)

```json
"@dengage-tech/react-native-dengage": "file:../dengage-react-sdk"
```

---

## 3. Backend URL configuration

The native SDKs read API and feature endpoints from your app configuration. Use the URLs supplied by your **Dengage / backend team** for each environment (development, staging, production).

### 3.1 Android — `AndroidManifest.xml`

Inside `<application>`, add one `<meta-data>` entry per key. Each `android:value` must be the URL your team provides.

| `android:name` | Purpose |
| ---------------- | ------- |
| `den_event_api_url` | Event ingestion (page view, cart, orders, custom events). |
| `den_push_api_url` | Push registration and delivery. |
| `den_device_id_api_url` | Device / contact registration and sync. |
| `den_in_app_api_url` | In-app message fetch and display. |
| `den_geofence_api_url` | Geofence campaigns (if geofence is enabled). |
| `fetch_real_time_in_app_api_url` | Real-time in-app (`showRealTimeInApp`). |

Example (replace placeholders with real URLs):

```xml
<meta-data
    android:name="den_event_api_url"
    android:value="https://YOUR_EVENT_URL" />
<meta-data
    android:name="den_push_api_url"
    android:value="https://YOUR_PUSH_URL" />
<meta-data
    android:name="den_device_id_api_url"
    android:value="https://YOUR_DEVICE_ID_URL" />
<meta-data
    android:name="den_in_app_api_url"
    android:value="https://YOUR_IN_APP_URL" />
<meta-data
    android:name="den_geofence_api_url"
    android:value="https://YOUR_GEOFENCE_URL" />
<meta-data
    android:name="fetch_real_time_in_app_api_url"
    android:value="https://YOUR_RT_IN_APP_URL" />
```

### 3.2 iOS — `Info.plist`

Add string keys and values (URLs from your team):

| Key | Purpose |
| --- | ------- |
| `DengageApiUrl` | Push / primary API base. |
| `DengageDeviceIdApiUrl` | Device identity. |
| `DengageEventApiUrl` | Event ingestion. |
| `DengageGeofenceApiUrl` | Geofence (if used). |
| `DengageInAppApiUrl` | In-app messaging. |
| `fetchRealTimeINAPPURL` | Real-time in-app. |

Example:

```xml
<key>DengageApiUrl</key>
<string>https://YOUR_PUSH_OR_MAIN_URL</string>
<key>DengageDeviceIdApiUrl</key>
<string>https://YOUR_DEVICE_ID_URL</string>
<key>DengageEventApiUrl</key>
<string>https://YOUR_EVENT_URL</string>
<key>DengageGeofenceApiUrl</key>
<string>https://YOUR_GEOFENCE_URL</string>
<key>DengageInAppApiUrl</key>
<string>https://YOUR_IN_APP_URL</string>
<key>fetchRealTimeINAPPURL</key>
<string>https://YOUR_RT_IN_APP_URL</string>
```

Datacenter-specific endpoint lists are documented on the Dengage developer site under **API Endpoints by Datacenter**.

---

## 4. Android integration

### 4.1 Repositories and Firebase

- Add **Google** and **JitPack** repositories if not already present (root / `settings.gradle` or project `build.gradle`, depending on your React Native template).
- Complete Firebase setup for Android: place `google-services.json` under `android/app/`.
- Apply the Google Services plugin on the **app** module:

```gradle
plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    // ...
}
```

In the project-level Gradle file, include the Google Services classpath version expected by your React Native / AGP setup (for example `com.google.gms:google-services:4.4.2`).

### 4.2 Native SDK version

The React Native package depends on **Dengage Android SDK** `6.0.88` (JitPack: `com.github.dengage-tech.dengage-android-sdk:sdk:6.0.88`). You normally do not add this line yourself unless you override versions; the library module brings it in.

### 4.3 Geofence (optional)

In **`android/gradle.properties`** (project root):

```properties
INSTALL_DENGAGE_GEOFENCE=true
```

This adds `sdk-geofence` alongside the core SDK. Without it, `startGeofence`, `stopGeofence`, and `requestLocationPermissions` will not find the geofence classes.

### 4.4 `AndroidManifest.xml`

**FCM service** inside `<application>`:

```xml
<service
    android:name="com.dengage.sdk.push.FcmMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```

**Optional — HMS** (Huawei): register `com.dengage.sdk.HmsMessagingService` with `com.huawei.push.action.MESSAGING_EVENT` if you integrate HMS.

**Carousel / push actions**: register a receiver that extends `com.dengage.sdk.push.NotificationReceiver` (see [Section 6](#6-rich-push-and-carousel-push)). Example:

```xml
<receiver
    android:name=".PushNotificationReceiver"
    android:exported="false">
    <intent-filter>
        <action android:name="com.dengage.push.intent.RECEIVE" />
        <action android:name="com.dengage.push.intent.OPEN" />
        <action android:name="com.dengage.push.intent.DELETE" />
        <action android:name="com.dengage.push.intent.ACTION_CLICK" />
        <action android:name="com.dengage.push.intent.ITEM_CLICK" />
        <action android:name="com.dengage.push.intent.CAROUSEL_ITEM_CLICK" />
    </intent-filter>
</receiver>
```

Use your application package for `android:name` (for example `com.company.app.PushNotificationReceiver`).

Add all [endpoint `meta-data`](#31-android--androidmanifestxml) entries under `<application>`.

### 4.5 Initialize Dengage in `Application`

In your **`Application`** subclass (commonly `MainApplication.kt`), you must:

1. Call **`DengageRNCoordinator.sharedInstance.injectReactInstanceManager(...)`** with your `ReactNativeHost`’s `reactInstanceManager` so the bridge is ready for notification events.
2. Call **`DengageRNCoordinator.sharedInstance.setupDengage(...)`** once (typically in `onCreate`).

**Important:** `firebaseIntegrationKey` must be non-null. If you do not use FCM yet, you still need a valid key as required by the coordinator.

```kotlin
import com.dengagetech.reactnativedengage.DengageRNCoordinator
import com.dengage.hms.DengageHmsManager
import com.facebook.react.ReactApplication

class MainApplication : Application(), ReactApplication {

    override fun onCreate() {
        super.onCreate()
        // ... SoLoader, New Architecture load, etc.

        DengageRNCoordinator.sharedInstance.injectReactInstanceManager(
            reactNativeHost.reactInstanceManager
        )

        DengageRNCoordinator.sharedInstance.setupDengage(
            firebaseIntegrationKey = "YOUR_FIREBASE_INTEGRATION_KEY",
            huaweiIntegrationKey = null,
            context = this,
            dengageHmsManager = null,
            deviceConfigurationPreference =
                com.dengage.sdk.data.remote.api.DeviceConfigurationPreference.Google,
            disableOpenWebUrl = false,
            logEnabled = false,
            enableGeoFence = false,
            developmentStatus = false
        )
    }

    // ...
}
```

**Huawei:** set `huaweiIntegrationKey` to your HMS integration key, pass a non-null `IDengageHmsManager` implementation (for example `DengageHmsManager()`), add HMS Gradle plugin and `agconnect-services.json`, and depend on the HMS artifacts your project uses.

**Parameters:**

| Parameter | Description |
| --------- | ----------- |
| `firebaseIntegrationKey` | Firebase / FCM integration key from Dengage (required). |
| `huaweiIntegrationKey` | HMS key, or `null` if not used. |
| `context` | Prefer `Application` so activity lifecycle tracking can be registered. |
| `dengageHmsManager` | HMS manager instance, or `null`. |
| `deviceConfigurationPreference` | Usually `Google` for Google Play builds. |
| `disableOpenWebUrl` | If `true`, suppresses opening URLs in a browser on certain push flows. |
| `logEnabled` | Enables Android SDK Logcat logging. |
| `enableGeoFence` | If `true`, starts geofence when `sdk-geofence` is present. |
| `developmentStatus` | Debug / development flag forwarded to the native SDK. |

---

## 5. iOS integration

### 5.1 Pod dependency

The **`react-native-dengage`** pod pulls in **Dengage `5.90`** (see `react-native-dengage.podspec`). Enabling geofence adds **DengageGeofence** when `install_dengage_geofence=1` is set during `pod install`.

Set a reasonable minimum iOS version in your `Podfile` (for example `platform :ios, '13.0'` or higher as required by your app).

### 5.2 Capabilities and background

In Xcode for the main app target:

- Enable **Push Notifications**.
- Enable **Background Modes → Remote notifications** (if you need background delivery behavior).

### 5.3 `Info.plist` endpoints

Add the [plist keys from Section 3.2](#32-ios--infoplist).

For **geofence / location**, add usage descriptions such as `NSLocationWhenInUseUsageDescription` and, if needed, `NSLocationAlwaysAndWhenInUseUsageDescription`.

### 5.4 Initialize with `DengageRNCoordinator`

Import the umbrella module from the pod (Swift: `import react_native_dengage`; Objective-C: `@import react_native_dengage;`).

At launch you must:

- Set `UNUserNotificationCenter.current().delegate` (often your `AppDelegate`) **before** or as part of startup.
- Call **`DengageRNCoordinator.staticInstance`** `setupDengage:appGroupsKey:launchOptions:application:askNotificationPermission:enableGeoFence:disableOpenURL:badgeCountReset:logVisible:` with your **iOS integration key** and an **App Group** identifier shared with notification extensions.

Example **Objective-C** (`AppDelegate`):

```objc
@import react_native_dengage;

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  UNUserNotificationCenter.currentNotificationCenter.delegate = self;

  DengageRNCoordinator *coordinator = [DengageRNCoordinator staticInstance];
  [coordinator setValue:launchOptions forKey:@"launchOptions"];
  [coordinator setupDengage:@"YOUR_IOS_INTEGRATION_KEY"
               appGroupsKey:@"group.com.yourcompany.yourapp.dengage"
              launchOptions:launchOptions
                application:application
  askNotificationPermission:YES
             enableGeoFence:NO
             disableOpenURL:NO
            badgeCountReset:NO
                 logVisible:NO];

  return YES;
}

- (void)application:(UIApplication *)application
    didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
  [[DengageRNCoordinator staticInstance] registerForPushToken:deviceToken];
}

- (void)userNotificationCenter:(UNUserNotificationCenter *)center
       willPresentNotification:(UNNotification *)notification
         withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler {
  completionHandler(UNNotificationPresentationOptionAlert |
                    UNNotificationPresentationOptionSound |
                    UNNotificationPresentationOptionBadge);
}

- (void)userNotificationCenter:(UNUserNotificationCenter *)center
didReceiveNotificationResponse:(UNNotificationResponse *)response
         withCompletionHandler:(void (^)(void))completionHandler {
  [[DengageRNCoordinator staticInstance] didReceivePush:center
                                               response:response
                                  withCompletionHandler:completionHandler];
}
```

**Parameters:**

| Parameter | Description |
| --------- | ----------- |
| Integration key | iOS app key from Dengage. |
| `appGroupsKey` | App Group used by the Notification Service Extension and shared storage (must match extensions). |
| `askNotificationPermission` | If `YES`, the SDK prompts for notification permission at startup. |
| `enableGeoFence` | Starts geofence when **DengageGeofence** is linked. |
| `disableOpenURL` | Maps to `DengageOptions.disableOpenURL`. |
| `badgeCountReset` | Maps to `DengageOptions.badgeCountReset`. |
| `logVisible` | Enables native SDK logging. |

You may wrap these calls in a small helper class (as in the bundled example app) to keep `AppDelegate` thin.

### 5.5 App Groups

Create an **App Group** in Apple Developer portal (for example `group.com.yourcompany.yourapp.dengage`). Add the capability to the main app and to the **Notification Service Extension**. The string passed to `setupDengage` must match.

---

## 6. Rich push and carousel push

### 6.1 Rich push (Android)

FCM delivers the payload to `FcmMessagingService`; the Dengage SDK can render expanded layouts when the payload includes media. Ensure manifest, endpoints, and Firebase key are correct.

### 6.2 Rich push (iOS) — Notification Service Extension

iOS does not attach remote images automatically. Add a **Notification Service Extension** target. In `NotificationService.swift`, call the Dengage API so media can be downloaded and attached before display.

Set the extension’s `DengageLocalStorage` app group to the **same** App Group name as in `setupDengage`. Link the **Dengage** pod for the extension target (matching major version **5.90**).

Example pattern:

```swift
import UserNotifications
import Dengage

class NotificationService: UNNotificationServiceExtension {
    var contentHandler: ((UNNotificationContent) -> Void)?
    var bestAttemptContent: UNMutableNotificationContent?

    override func didReceive(
        _ request: UNNotificationRequest,
        withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void
    ) {
        self.contentHandler = contentHandler
        bestAttemptContent = request.content.mutableCopy() as? UNMutableNotificationContent
        if let content = bestAttemptContent {
            DengageLocalStorage.shared.setAppGroupsUserDefaults(
                appGroupName: "group.com.yourcompany.yourapp.dengage"
            )
            Dengage.didReceiveNotificationRequest(content, withContentHandler: contentHandler)
        }
    }

    override func serviceExtensionTimeWillExpire() {
        if let contentHandler = contentHandler,
           let bestAttemptContent = bestAttemptContent {
            contentHandler(bestAttemptContent)
        }
    }
}
```

Align `NSExtension` keys in the plan extension `Info.plist` with Apple’s template for Notification Service extensions.

### 6.3 Carousel (Android) — detailed guide

Carousel notifications show **several items** (image URL, title, description, target URL). The user can move **previous / next** and **tap** the current item. The Dengage **FCM** path still receives the message; when the payload is a carousel, the SDK **does not draw the final UI for you** — it calls **`onCarouselRender`** on **your** subclass of **`com.dengage.sdk.push.NotificationReceiver`** so you build the notification with **`RemoteViews`**.

#### How the flow works

1. FCM delivers the message to **`com.dengage.sdk.push.FcmMessagingService`** (declared in your manifest).
2. The SDK parses the payload. For carousel campaigns it delivers intents to your **`NotificationReceiver`** subclass.
3. For the **carousel UI**, **`onCarouselRender(...)`** is invoked with:
   - **`message`**: overall notification (title, body, sound, channel data, etc.).
   - **`leftCarouselItem`**, **`currentCarouselItem`**, **`rightCarouselItem`**: three **adjacent** slots in the carousel (`CarouselItem`: image URL, title, description, link, etc.). The SDK tells you what to show left / center / right after the user navigates.
4. You inflate **collapsed** and **expanded** layouts as **`RemoteViews`**, assign **pending intents** for tap / prev / next, **load images asynchronously**, then **`NotificationManagerCompat.notify(...)`**.

If you **do not** implement **`onCarouselRender`** (or your manifest does not route carousel actions), users will not see your custom carousel UI.

#### Step 1 — Register the receiver in `AndroidManifest.xml`

Inside **`<application>`**, declare a **`<receiver>`** whose `android:name` is your class (Kotlin package + class name). It must be **`android:exported="false"`** and list **all** of these actions (carousel **requires** `CAROUSEL_ITEM_CLICK` and `ITEM_CLICK`):

```xml
<receiver
    android:name=".PushNotificationReceiver"
    android:exported="false">
    <intent-filter>
        <action android:name="com.dengage.push.intent.RECEIVE" />
        <action android:name="com.dengage.push.intent.OPEN" />
        <action android:name="com.dengage.push.intent.DELETE" />
        <action android:name="com.dengage.push.intent.ACTION_CLICK" />
        <action android:name="com.dengage.push.intent.ITEM_CLICK" />
        <action android:name="com.dengage.push.intent.CAROUSEL_ITEM_CLICK" />
    </intent-filter>
</receiver>
```

Use your real class name, e.g. **`com.yourcompany.app.PushNotificationReceiver`**.

#### Step 2 — Add layout XML under `res/layout/`

Notifications use **`RemoteViews`**, so layouts must only use **supported widgets** (for example `LinearLayout`, `RelativeLayout`, `FrameLayout`, `TextView`, `ImageView`). You need at least:

| File | Role |
| ---- | ---- |
| **`den_carousel_collapsed.xml`** | Small **collapsed** header: main title + body (and optionally a small icon). |
| **`den_carousel_portrait.xml`** | **Expanded** carousel: left / **current** / right images, prev/next controls, **current** item title + description. |

Optional: **`den_carousel_landscape.xml`** if you want a different expanded layout in landscape.

The **example app** in this repo (`example/android/app/src/main/res/layout/`) contains working **`den_carousel_*.xml`** files you can **copy** and restyle. Your **Kotlin code must use the same `@+id/...` names** it binds to (see below).

**Collapsed layout — IDs used by the sample receiver**

- **`den_carousel_title`** — main notification title (`message.title`).
- **`den_carousel_message`** — main body (`message.message`).
- **`den_carousel_image`** — optional small icon in header (set image in XML or via `RemoteViews` if you add code for it).

**Portrait expanded layout — IDs used by the sample receiver**

- **`den_carousel_title`**, **`den_carousel_message`** — same as collapsed (often duplicated in expanded view).
- **`den_carousel_portrait_left_image`**, **`den_carousel_portrait_current_image`**, **`den_carousel_portrait_right_image`** — the three carousel images.
- **`den_carousel_left_arrow`**, **`den_carousel_right_arrow`** — prev / next hits.
- **`den_carousel_item_title`**, **`den_carousel_item_description`** — **current** item text (`currentCarouselItem`).

If you rename IDs in XML, **update every** `R.id.*` reference in **`onCarouselRender`**.

**Drawables / mipmaps**

- Add **left/right arrow** drawables (e.g. `ic_arrow_left`, `ic_arrow_right`) referenced from XML.
- **`NotificationCompat.Builder.setSmallIcon(...)`** must use a **valid small icon** (often `R.mipmap.ic_launcher` or a **white-on-transparent** notification icon per Material guidelines).

#### Step 3 — Implement `PushNotificationReceiver` (Kotlin)

Create a class in your app module that **extends** **`NotificationReceiver()`** and **override** **`onCarouselRender`**. Call **`super.onCarouselRender(...)`** first (recommended so base behavior stays consistent).

**Helpers from the base `NotificationReceiver`** (you do not reimplement these):

- **`getItemClickIntent`**, **`getLeftItemIntent`**, **`getRightItemIntent`**, **`getDeleteIntent`**, **`getContentIntent`** — build intents from **`intent.extras`** and your **`packageName`**.
- **`getPendingIntent`**, **`getCarouselDirectionIntent`**, **`getDeletePendingIntent`** — wrap intents as **`PendingIntent`** with stable request codes for carousel navigation.
- **`createNotificationChannel(context, message)`** — returns a **channel id** for Android 8+ (uses message metadata when available).
- **`loadCarouselImageToView(carouselView, imageViewId, carouselItem, onComplete)`** — downloads the **carousel item** image URL and applies it to the **`RemoteViews`** image view **asynchronously**.

**Typical implementation pattern** (matches the bundled **`example`** app):

```kotlin
package com.yourcompany.app

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dengage.sdk.domain.push.model.CarouselItem
import com.dengage.sdk.domain.push.model.Message
import com.dengage.sdk.push.NotificationReceiver
import com.dengage.sdk.push.getSoundUri

class PushNotificationReceiver : NotificationReceiver() {

    override fun onCarouselRender(
        context: Context,
        intent: Intent,
        message: Message,
        leftCarouselItem: CarouselItem,
        currentCarouselItem: CarouselItem,
        rightCarouselItem: CarouselItem
    ) {
        super.onCarouselRender(
            context, intent, message,
            leftCarouselItem, currentCarouselItem, rightCarouselItem
        )

        val itemTitle = currentCarouselItem.title
        val itemDesc = currentCarouselItem.description

        val itemIntent = getItemClickIntent(intent.extras, context.packageName)
        val leftIntent = getLeftItemIntent(intent.extras, context.packageName)
        val rightIntent = getRightItemIntent(intent.extras, context.packageName)
        val deleteIntent = getDeleteIntent(intent.extras, context.packageName)
        val contentIntent = getContentIntent(intent.extras, context.packageName)

        val carouseItemIntent = getPendingIntent(context, 0, itemIntent)
        val carouselLeftIntent = getCarouselDirectionIntent(context, 1, leftIntent)
        val carouselRightIntent = getCarouselDirectionIntent(context, 2, rightIntent)
        val deletePendingIntent = getDeletePendingIntent(context, 4, deleteIntent)
        val contentPendingIntent = getPendingIntent(context, 5, contentIntent)

        val collapsedView = RemoteViews(context.packageName, R.layout.den_carousel_collapsed)
        collapsedView.setTextViewText(R.id.den_carousel_title, message.title)
        collapsedView.setTextViewText(R.id.den_carousel_message, message.message)

        val carouselView = RemoteViews(context.packageName, R.layout.den_carousel_portrait)
        carouselView.setTextViewText(R.id.den_carousel_title, message.title)
        carouselView.setTextViewText(R.id.den_carousel_message, message.message)
        carouselView.setTextViewText(R.id.den_carousel_item_title, itemTitle)
        carouselView.setTextViewText(R.id.den_carousel_item_description, itemDesc)

        carouselView.setOnClickPendingIntent(R.id.den_carousel_left_arrow, carouselLeftIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_portrait_current_image, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_title, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_item_description, carouseItemIntent)
        carouselView.setOnClickPendingIntent(R.id.den_carousel_right_arrow, carouselRightIntent)

        val channelId = createNotificationChannel(context, message)

        loadCarouselImageToView(
            carouselView, R.id.den_carousel_portrait_left_image, leftCarouselItem, onComplete = { }
        )
        loadCarouselImageToView(
            carouselView, R.id.den_carousel_portrait_current_image, currentCarouselItem, onComplete = { }
        )
        loadCarouselImageToView(
            carouselView, R.id.den_carousel_portrait_right_image, rightCarouselItem, onComplete = { }
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(carouselView)
            .setContentIntent(contentPendingIntent)
            .setDeleteIntent(deletePendingIntent)
            .setSound(message.sound.getSoundUri(context))
            .build()

        notification.flags = Notification.FLAG_AUTO_CANCEL or Notification.FLAG_ONLY_ALERT_ONCE

        val notificationManager = NotificationManagerCompat.from(context)
        intent.extras?.getInt("requestCode")?.let { notificationManager.notify(it, notification) }
    }
}
```

#### Step 4 — Pending intents (what each is for)

| Pending intent | Typical use |
| --------------- | ----------- |
| **Content / open** | User taps the notification body / main area — open the app or deep link from **`contentIntent`**. |
| **Item click** | User taps the **current** carousel image or title — open **that item’s** URL / screen. |
| **Left / right** | **`getCarouselDirectionIntent`** — SDK uses these to **request the previous/next slice** of the carousel; the receiver may be called again with updated **`CarouselItem`**s. |
| **Delete** | User dismisses the notification. |

Use the **same request-code pattern** as above (`0`, `1`, `2`, …) so multiple **`PendingIntent`s** do not overwrite each other incorrectly.

#### Step 5 — Images and `notify`

**`loadCarouselImageToView`** updates **`RemoteViews`** when each bitmap is ready. On slow networks, the notification may first appear **without** images; you can call **`notificationManager.notify(requestCode, updatedBuilder.build())`** again inside **`onComplete`** if you need to refresh when all images loaded (optional optimization).

Always use **`intent.extras?.getInt("requestCode")`** (as in the sample) for **`notify(id, ...)`** so updates replace the same notification when the user swipes prev/next.

#### Step 6 — Campaign / panel checklist

- Send a **carousel**-type push from Dengage; payload must match what the SDK expects for carousel (your integration team can confirm).
- **HTTPS** image URLs must be reachable from the device.
- If nothing calls **`onCarouselRender`**: verify **receiver** name, **intent-filter** actions, **`CAROUSEL_ITEM_CLICK`**, and that **`Dengage.init` / `setupDengage`** ran with a valid Firebase key.

#### Common Android carousel mistakes

| Problem | Likely cause |
| ------- | ------------- |
| Carousel never shows custom UI | Receiver missing **`CAROUSEL_ITEM_CLICK`** or wrong `android:name` package. |
| Crashes in **`RemoteViews`** | Unsupported views, bad **layout depth**, or wrong **resource** references. |
| Taps do nothing | **`setOnClickPendingIntent`** not set for the touched view id; or **`PendingIntent` mutability** issues on newer Android (ensure flags match your `targetSdk`). |
| Images blank | Wrong **R.id**; URL blocked; or need second **`notify`** after **`loadCarouselImageToView`** completes. |

---

### 6.4 Carousel (iOS) — Notification Content Extension (detailed)

On iOS, the **lock screen / banner** only shows default title and body. For a **swipeable carousel**, you need a **Notification Content Extension**. The **category identifier** in the push payload must **exactly** match **`UNNotificationExtensionCategory`** in the extension’s **`Info.plist`** (for example `DENGAGE_CAROUSEL_CATEGORY`). Your Dengage campaign must use that same category.

#### Step 1 — Create the extension target

In Xcode: **File → New → Target → Notification Content Extension**. Name it (e.g. `NotificationContentExtension`). This adds a small **`Info.plist`**, a default **`NotificationViewController`**, and optionally a storyboard.

#### Step 2 — Configure the extension `Info.plist`

Under **`NSExtension`**:

- **`NSExtensionPointIdentifier`**: `com.apple.usernotifications.content-extension`
- **`NSExtensionAttributes`**:
  - **`UNNotificationExtensionCategory`**: your carousel category string (**must match** the backend).
  - **`UNNotificationExtensionDefaultContentHidden`**: often `true` if your UI replaces default title/body.
  - Optional: **`UNNotificationExtensionInitialContentSizeRatio`**, **`UNNotificationExtensionUserInteractionEnabled`**.

Set **`NSExtensionPrincipalClass`** (or main storyboard) to your view controller.

If your build requires it, duplicate the same **Dengage URL keys** in this extension’s **`Info.plist`** as in the main app (see [§3.2](#32-ios--infoplist)).

#### Step 3 — Link the **Dengage** pod for the extension

In **`Podfile`**, add a target for the content extension and depend on **`Dengage`** with the **same version** as the main app (the React Native pod uses **`5.90`**). Run **`pod install`**.

```ruby
target 'YourContentExtension' do
  pod 'Dengage', '5.90'
end
```

#### Step 4 — Implement the view controller (Option A — recommended)

Use **`DengageNotificationCarouselView`** so you do not parse JSON manually. The **`@objc(...)`** name must match **`NSExtensionPrincipalClass`** / storyboard if applicable.

```swift
import UIKit
import UserNotifications
import UserNotificationsUI
import Dengage

@objc(NotificationViewController)
class NotificationViewController: UIViewController, UNNotificationContentExtension {

    let carouselView = DengageNotificationCarouselView.create()

    func didReceive(_ notification: UNNotification) {
        DengageLocalStorage.shared.setAppGroupsUserDefaults(
            appGroupName: "group.com.yourcompany.yourapp.dengage"
        )
        Dengage.setIntegrationKey(key: "YOUR_IOS_INTEGRATION_KEY")
        Dengage.setLog(isVisible: false)
        carouselView.didReceive(notification)
    }

    func didReceive(
        _ response: UNNotificationResponse,
        completionHandler completion: @escaping (UNNotificationContentExtensionResponseOption) -> Void
    ) {
        carouselView.didReceive(response, completionHandler: completion)
    }

    override func loadView() {
        self.view = carouselView
    }
}
```

**App Group** string must be the **same** as in **`setupDengage`** / **`DengageRNCoordinator`** and your **Notification Service Extension** (if you use **`DengageLocalStorage`** there too).

#### Step 5 — Option B — fully custom UI

Parse **`notification.request.content.userInfo`** (carousel array key depends on payload; align with your integration team), drive a **`UICollectionView`**, and implement **`didReceive(_:UNNotificationResponse:completionHandler:)`** for action / tap handling. You must still match **`UNNotificationExtensionCategory`** to receive carousel notifications.

#### Step 6 — Rich media + carousel

**Rich images** in the banner often need a **Notification Service Extension** (see [§6.2](#62-rich-push-ios--notification-service-extension)) **in addition** to the content extension. Use the **same** **`Dengage`** version and **App Group** across app + extensions.

#### Common iOS carousel mistakes

| Problem | Likely cause |
| ------- | ----------- |
| Extension never loads | **Category** in payload ≠ **`UNNotificationExtensionCategory`**. |
| Blank carousel | Wrong integration key in extension; or **App Group** mismatch for storage. |
| Build errors | Content extension not linked to **Dengage** pod; version mismatch vs main target. |

---

## 7. JavaScript / TypeScript API

Default import (native module):

```typescript
import Dengage from '@dengage-tech/react-native-dengage';
```

Exported UI components and types:

```typescript
import {
  InAppInlineView,
  StoriesListView,
  type Subscription,
  type InboxMessage,
  type Cart,
  type SdkParameters,
} from '@dengage-tech/react-native-dengage';
```

Call methods after native initialization (for navigation-heavy APIs, when an `Activity` / screen is active).

### 7.1 Identity, device, logging

| Method | Description |
| ------ | ----------- |
| `setContactKey(key: string \| null)` | Bind the device to your user / contact id. Call on login and when user identity is known. |
| `getContactKey()` | `Promise<string \| null>` — contact key. |
| `setDeviceId(deviceId: string)` | Custom device identifier. |
| `getDeviceId()` | `Promise<string>` — device id from subscription where available. |
| `setPartnerDeviceId(adid: string)` | Partner / advertising id. |
| `setFirebaseIntegrationKey(key: string)` | **Android:** updates key (prefer setting at init). |
| `setIntegrationKey(key: string)` | **iOS:** integration key from JS. |
| `getIntegrationKey()` | **Android / iOS:** `Promise<string>`. |
| `setLogStatus(isVisible: boolean)` | Enable or disable native logs. |
| `setDevelopmentStatus(isDebug: boolean)` | Development / debug flag. |
| `setLanguage(language: string)` | Language hint for the SDK. |
| `getSdkVersion()` | `Promise<string>`. |
| `getSdkParameters()` | `Promise<SdkParameters \| null>` — remote SDK configuration snapshot. |

### 7.2 Push and permission

| Method | Description |
| ------ | ----------- |
| `getToken()` | `Promise<string>` — FCM / APNs token string. |
| `setToken(token: string)` | Set token manually if needed. |
| `promptForPushNotifications()` | Request notification permission (platform-specific). |
| `promptForPushNotificationsWitCallback(cb: (hasPermission: boolean) => void)` | **iOS:** permission result callback. |
| `registerForRemoteNotifications(enable: boolean)` | **iOS:** enable / disable remote registration. |
| `setUserPermission(permission: boolean)` | Opt-in / permission flag. |
| `getUserPermission()` | `Promise<boolean>`. |
| `getLastPushPayload()` | `Promise<string>` — last push payload string. |
| `resetAppBadge()` | **Android:** clears notifications (implementation uses `cancelAll`). |

### 7.3 Commerce and analytics events

| Method | Description |
| ------ | ----------- |
| `pageView(params: object)` | Page / screen events (`page_type`, etc.). |
| `addToCart(params: object)` | Add to cart. |
| `removeFromCart(params: object)` | Remove from cart. |
| `viewCart(params: object)` | View cart. |
| `beginCheckout(params: object)` | Checkout start. |
| `placeOrder(params: object)` | Completed order. |
| `cancelOrder(params: object)` | Order cancellation. |
| `addToWishList(params: object)` | Wishlist add. |
| `removeFromWishList(params: object)` | Wishlist remove. |
| `search(params: object)` | Search event. |
| `sendDeviceEvent(tableName: string, data: object)` | Custom table event (columns depend on Big Data table schema). |
| `sendCustomEvent(eventTable: string, key: string, parameters: object)` | Custom event with explicit key. |
| `onMessageReceived(params: object)` | **Android:** forward FCM-style data map to native (`Dengage.onMessageReceived`). |

### 7.4 In-app navigation and real-time in-app

| Method | Description |
| ------ | ----------- |
| `setNavigation()` | Notify current activity / screen (no name). |
| `setNavigationWithName(screenName: string)` | Set logical screen name for targeting. Call on every navigation when you use screen filters. |
| `setInAppDeviceInfo(key: string, value: string)` | Key / value for in-app targeting. |
| `clearInAppDeviceInfo()` | Clear custom in-app device info. |
| `getInAppDeviceInfo()` | `Promise<Record<string, string>>`. |
| `setCategoryPath(path: string)` | Category path for campaigns. |
| `setCartItemCount(count: string)` | Cart item count (string as expected by native API). |
| `setCartAmount(amount: string)` | Cart total amount string. |
| `setState(state: string)` | Region / state context. |
| `setCity(city: string)` | City context. |
| `showRealTimeInApp(screenName: string, params: Record<string, string>)` | Request a real-time in-app for the screen. |
| `registerInAppListener()` | Register so in-app deep link taps emit **`retrieveInAppLink`** in JS. **Android:** registers a broadcast receiver for `com.dengage.inapp.LINK_RETRIEVAL`. **iOS:** wraps `Dengage.handleInAppDeeplink` and forwards URLs via the module event emitter. Call once when you need link callbacks (see [Section 9](#9-events-inbox-deep-links)). |
| `setInAppLinkConfiguration(deeplink: string)` | Deep link / URL handling configuration for in-app actions. |

### 7.5 Cart object API

| Method | Description |
| ------ | ----------- |
| `setCart(cart: Cart)` | `Promise<boolean>` — pushes structured cart (items + summary). |
| `getCart()` | `Promise<Cart>` — read normalized cart from native. |

`Cart` / `CartItem` / `CartSummary` types are defined in the package’s TypeScript definitions (`CartItem` includes pricing, quantity, attributes, computed totals, category segments).

### 7.6 Inbox

| Method | Description |
| ------ | ----------- |
| `getInboxMessages(offset: number, limit: number)` | `Promise<InboxMessage[]>` — paginated inbox. |
| `deleteInboxMessage(id: string)` | `Promise<boolean>`. |
| `setInboxMessageAsClicked(id: string)` | `Promise<boolean>`. |
| `deleteAllInboxMessages()` | `Promise<boolean>`. |
| `setAllInboxMessageAsClicked()` | `Promise<boolean>`. |

`InboxMessage` includes `id`, `title`, `message`, `mediaURL`, `targetUrl`, `receiveDate`, `isClicked`, `carouselItems`, `customParameters`.

### 7.7 Geofence

| Method | Description |
| ------ | ----------- |
| `requestLocationPermissions()` | Request location (requires geofence artifact). |
| `startGeofence()` | Start geofence (reflection / native). |
| `stopGeofence()` | Stop geofence. |

Requires native geofence dependencies and location permission strings.

### 7.8 iOS notification actions

| Method | Description |
| ------ | ----------- |
| `handleNotificationActionBlock(callback: (action) => void)` | **iOS:** receive structured notification action payloads. |

---

## 8. In-app inline and App Story

### 8.1 In-app inline

Render a native inline placement where you want a banner or slot. Pass the **property id** and **screen name** configured in Dengage. Call `setNavigationWithName` with the same screen name when that screen is visible.

```tsx
import { InAppInlineView } from '@dengage-tech/react-native-dengage';

<InAppInlineView
  propertyId="1"
  screenName="home-inline"
  customParams={{}}
  style={{ height: 244, width: '100%' }}
/>;
```

Give the view a **bounded height** (for example 244) so layout is stable.

### 8.2 App Story

Horizontal story list; configure the template in the Dengage panel and match **property id** and **screen name**.

```tsx
import { StoriesListView } from '@dengage-tech/react-native-dengage';

<StoriesListView
  storyPropertyId="4"
  screenName="appstory"
  customParams={{}}
  style={{ minHeight: 160 }}
/>;
```

Props may be `null` in type definitions when you load configuration dynamically; pass strings when known.

---

## 9. Events, inbox, deep links

### 9.1 `NativeEventEmitter` (JavaScript)

The native module implements **`RCTEventEmitter`** on iOS and registers listeners on Android. Event names:

| Event | When |
| ----- | ---- |
| `onNotificationClicked` | User opens a notification (payload forwarded to JS). |
| `onNotificationReceived` | **Android:** notification received (when listeners are registered). |
| `retrieveInAppLink` | In-app link tap; payload includes `targetUrl`. After calling **`registerInAppListener()`** on both Android and iOS. |

Example:

```tsx
import { NativeEventEmitter, NativeModules } from 'react-native';

const emitter = new NativeEventEmitter(NativeModules.DengageRN);

useEffect(() => {
  const subClick = emitter.addListener('onNotificationClicked', (e) => {
    // Navigate or log
  });
  const subLink = emitter.addListener('retrieveInAppLink', (e) => {
    const url = e?.targetUrl;
  });
  return () => {
    subClick.remove();
    subLink.remove();
  };
}, []);
```

Call **`Dengage.registerInAppListener()`** on **Android and iOS** so `retrieveInAppLink` is delivered when the user taps an in-app link (implementation differs by platform; see the API table above).

The package automatically invokes native **`registerNotificationListeners`** when the module loads so basic push events can flow; carousel handling still requires your **`NotificationReceiver`** implementation.

### 9.2 Inbox behavior

Messages appear in the inbox when campaigns use **Save to Inbox** in the panel. Use `getInboxMessages` / delete / mark-clicked helpers to build your UI.

`receiveDate` is UTC; convert to local time for display.

### 9.3 Deep links

Use **`setInAppLinkConfiguration`** with your app scheme or universal link prefix so URLs open inside the app when appropriate. Combine with **`retrieveInAppLink`** to route in JavaScript.

---

## 10. Example app and troubleshooting

### 10.1 Run the bundled example

```bash
cd dengage-react-sdk/example
yarn install
# Android: add google-services.json under example/android/app/
# iOS: cd ios && pod install && cd ..
yarn android
yarn ios
```

Replace integration keys and endpoint URLs in:

- Android: `MainApplication.kt` (and `Constants` or equivalent).
- iOS: your `DengageRNCoordinator` / helper where the key is set.

### 10.2 Common issues

| Issue | What to check |
| ----- | ------------- |
| No push (Android) | `google-services.json`, FCM service in manifest, Firebase key in `setupDengage`, endpoint meta-data, Google Services plugin. |
| No push (iOS) | Push capability, APNs key in Dengage, `registerForPushToken`, correct `Info.plist` URLs, integration key in coordinator. |
| In-app never shows | `setNavigation` / `setNavigationWithName`, screen name matches campaign, endpoints correct. |
| Carousel (Android) | Custom `NotificationReceiver`, layouts, `CAROUSEL_ITEM_CLICK`, `onCarouselRender` implementation. |
| Rich / carousel (iOS) | Service extension calls `Dengage.didReceiveNotificationRequest`, App Group matches, content extension category matches campaign, Dengage pod version aligned. |
| Geofence missing | `INSTALL_DENGAGE_GEOFENCE=true` (Android), `install_dengage_geofence=1` before `pod install` (iOS), location strings in plist. |
| `retrieveInAppLink` silent | Call `registerInAppListener()` on **both** platforms; on Android verify the `LINK_RETRIEVAL` broadcast path; on iOS ensure `NativeEventEmitter` is subscribed and the Dengage in-app deeplink handler is active. |
| Bridge errors on startup | `injectReactInstanceManager` called before or with `setupDengage`; rebuild after native changes. |

### 10.3 Example app folders (reference)

```
example/
├── src/
│   ├── App.tsx                 # navigation, optional notification listener
│   └── screens/                # inbox, in-app, inline, story, geofence, cart, etc.
├── android/
│   └── app/                    # MainApplication, AndroidManifest, google-services
└── ios/
    ├── Podfile
    ├── ReactNativeDengageExample/
    │   ├── AppDelegate.mm
    │   └── DengageCoordinatorHelper.m
    └── (Notification extensions if enabled)
```

---

## Subscription model

The native layer maintains **subscription** state (token, device ids, contact key, permission flags). It is updated on init, contact key changes, token updates, and permission changes. Use **`getSubscription()`** to read a snapshot (`Subscription` type in the package). Some fields may be empty on one platform; always guard for missing values in production UI.

---

## License

MIT — see the `LICENSE` file in the repository.

---

## Sample app on GitHub

Runnable sample project (`example/`): **[dengage-react-sdk/example — branch `stable_branch_combined`](https://github.com/dengage-tech/dengage-react-sdk/tree/stable_branch_combined/example)**
