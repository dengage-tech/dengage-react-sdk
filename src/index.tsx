import { NativeModules } from 'react-native';
import { DengageTypes } from './types';

type DengageType = {
  multiply(a: number, b: number): Promise<number>;
  setIntegerationKey(key: string): void; // iOS only
  setHuaweiIntegrationKey(key: string): void; // android only
  setFirebaseIntegrationKey(key: string): void; // android only
  setContactKey(key: string): void;
  getContactKey(): Promise<string | null | undefined>;
  promptForPushNotifications(): void; // iOS only
  promptForPushNotificationsWitCallback(callback: (hasPermission: boolean) => void): void; // iOS only
  setUserPermission(permission: boolean): void;
  getUserPermission(): Promise<boolean>; // android only, in iOS use promptForPushNotificationsWitCallback
  getToken(): Promise<string | any>;
  setToken(token: String): void;
  setLogStatus(isVisible: boolean): void;
  handleNotificationActionBlock(callback: (notificationAction: DengageTypes["NotificationAction"]) => void): void; // iOS only
};

const { DengageRN } = NativeModules;

export default DengageRN as DengageType;
