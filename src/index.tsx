import { NativeModules } from 'react-native';

type DengageType = {
  multiply(a: number, b: number): Promise<number>;
  setIntegerationKey(key: string): void; // iOS only
  setHuaweiIntegrationKey(key: string): void; // android only
  setFirebaseIntegrationKey(key: string): void; // android only
  setContactKey(key: string): void;
  getContactKey(): Promise<string | null | undefined>;
  promptForPushNotifications(): void; // iOS only
  promptForPushNotificationsWitCallback(callback: 'function'): void; // iOS only
  setUserPermission(permission: boolean): void;
  getToken(): Promise<string | any>;
  setToken(token: String): void; // iOS only
  setLogStatus(isVisible: boolean): void;
};

const { DengageRN } = NativeModules;

export default DengageRN as DengageType;
