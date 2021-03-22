import { NativeModules } from 'react-native';

type DengageType = {
  multiply(a: number, b: number): Promise<number>;
  setIntegerationKey(key: string): void;
  promptForPushNotifications(): void;
  promptForPushNotificationsWitCallback(callback: 'function'): void;
  setUserPermission(permission: boolean): void;
  getToken(): Promise<string | any>;
  setToken(token: String): void;
  setLogStatus(isVisible: boolean): void;
};

const { DengageRN } = NativeModules;

export default DengageRN as DengageType;
