import { NativeModules } from 'react-native';

type DengageType = {
  multiply(a: number, b: number): Promise<number>;
  setIntegerationKey(key: string): void;
  promptForPushNotifications(): void;
  promptForPushNotificationsWitCallback(callback: func): void;
};

const { DengageRN } = NativeModules;

export default DengageRN as DengageType;
