# react-native-dengage

**dEngage Customer Driven Marketing Platform (CDMP)** serves as a customer data platform (CDP) with built-in omnichannel marketing features. It replaces your marketing automation and cross-channel campaign management.
The platform positions itself between our client’s data sources and its customers. It is also capable of exporting anonymous data to 3rd party networks such as **Oracle Bluekai, Criteo, Facebook Custom Audiences** and **Google Customer** Match to support retargeting and remarketing efforts of our clients.
For further details about dEngage please [visit here](https://docs.dengage.com).

This package makes it easy to integrate your native React-Native iOS and/or Android apps with dEngage. Following are instructions for installation of react-native-dengage SDK to your react-native applications.

## Installation

```sh
npm install react-native-dengage
```

## Linking

<details>
  <summary> iOS Linking </summary>
  
  ### React Native 0.60 and above
  Run npx ```pod-install```. Linking is not required in React Native 0.60 and above.
  
  ### React Native 0.59 and below
  Run react-native link react-native-dengage to link the react-native-dengage library.

</details>

<details>
  <summary> android Linking </summary>
  
</details>

## Platform Specific Extra Steps
Following extra steps after the installation of the react-native-dengage SDK are required for it to work properly.

<details>
  <summary> iOS Specific Extra steps </summary>
</details>

<details>
  <summary> android Specific Extra Steps </summary>
</details>

## Supported Versions 
<details>
  <summary> iOS </summary>
  
  D·engage Mobil SDK for IOS supports version IOS 10 and later.
</details>

<details>
  <summary> android </summary>
  
  D·engage Mobil SDK for Android supports version 4.4 (API Level 19) and later.

  <summary> Huawei </summary>
  
  D·engage Mobil SDK for Huawei supports all new versions.
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
