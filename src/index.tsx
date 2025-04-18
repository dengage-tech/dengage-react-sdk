import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package '@dengage-tech/react-native-dengage' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const DengageRN = NativeModules.DengageRN
  ? NativeModules.DengageRN
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return DengageRN.multiply(a, b);
}
export function promptForPushNotifications(): void {
  return DengageRN.promptForPushNotifications();
}
