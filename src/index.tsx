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
  registerForRemoteNotifications(enable: boolean): void; // iOS only
  getUserPermission(): Promise<boolean>; // android only, in iOS use promptForPushNotificationsWitCallback
  getToken(): Promise<string | any>;
  setToken(token: String): void;
  setLogStatus(isVisible: boolean): void;
  handleNotificationActionBlock(callback: (notificationAction: DengageTypes["NotificationAction"]) => void): void; // iOS only,
  registerNotificationListeners(): void; // End Developer No Need to call this, we're calling it ourself, to register for events like `onNotificationReceived` & `onNotificationClicked`
  pageView(params: object): void;
  addToCart(params: object): void;
  removeFromCart(params: object): void;
  viewCart(params: object): void;
  beginCheckout(params: object): void;
  placeOrder(params: object): void;
  cancelOrder(params: object): void;
  addToWishList(params: object): void;
  removeFromWishList(params: object): void;
  search(params: object): void;
  sendDeviceEvent(tableName: string, data: object): void;
  getSubscription(): Promise<object | null | undefined>; // android only yet. for iOS use getContactKey
  getInboxMessages(offset: number, limit: number): Promise<[object] | null>
  deleteInboxMessage(id: string): Promise<object | null>
  setInboxMessageAsClicked(id: string): Promise<object | null>
  setNavigation(): void;
  setNavigationWithName(screenName: string): void;
  onMessageRecieved(params: object): void;
  resetAppBadge():void; // android only
};

const { DengageRN } = NativeModules;

DengageRN?.registerNotificationListeners?.()

export default DengageRN as DengageType;
