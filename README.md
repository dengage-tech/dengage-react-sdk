# react-native-dengage

**dEngage Customer Driven Marketing Platform (CDMP)** serves as a customer data platform (CDP) with built-in omnichannel marketing features. It replaces your marketing automation and cross-channel campaign management.
The platform positions itself between our client’s data sources and its customers. It is also capable of exporting anonymous data to 3rd party networks such as **Oracle Bluekai, Criteo, Facebook Custom Audiences** and **Google Customer** Match to support retargeting and remarketing efforts of our clients.
For further details about dEngage please [visit here](https://docs.dengage.com).

This package makes it easy to integrate, dEngage, with your React-Native iOS and/or Android apps. Following are instructions for installation of react-native-dengage SDK to your react-native applications.

## Installation

```sh
npm install react-native-dengage
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
  
  1. Endpoint Configuration in PInfo.list
  For initial setup, if you have given URL addresses by dEngage Support team, you need to setup url address by using ```Info.plist``` file. Otherwise you don’t need to add anything to ```Info.plist``` file. Following screenshot for the keys in ```Info.plist``` file.
  
  ![Info.plist screenshot](https://raw.githubusercontent.com/whitehorse-technology/Dengage.Framework/master/docs/img/Screen%20Shot%202020-09-25%20at%2015.41.27.png)

> Note: Please see API Endpoints by Datacenter documentation in this section for end points. [here is link](https://dev.dengage.com/mobile-sdk/api-endpoints)

  2. Add Required Capabilities
  In Xcode, select the root project and main app target. In ***Signing & Capabilities***, select ***All*** and ***+ Capability***. Add "Push Notifications" and ***Background Modes***
  ![capabilities screenshot](https://files.readme.io/17798af-dengage_push_Step1.png)

#### Setting dEngage Integeration Key
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
