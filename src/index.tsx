import { NativeModules, Platform } from 'react-native';
import type { DengageTypes } from './types';

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

type DengageType = {
  setIntegrationKey(key: string): void; // iOS only
  setFirebaseIntegrationKey(key: string): void; // android only
  setContactKey(key: string): void;
  getContactKey(): Promise<string>;
  promptForPushNotifications(): void; // iOS only
  promptForPushNotificationsWitCallback(callback: (hasPermission: boolean) => void): void; // iOS only
  setUserPermission(permission: boolean): void;
  registerForRemoteNotifications(enable: boolean): void; // iOS only
  getUserPermission(): Promise<boolean>; // android only, in iOS use promptForPushNotificationsWitCallback
  getToken(): Promise<string>;
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
  getSubscription(): Promise<DengageTypes["Subscription"]>; // android only yet. for iOS use getContactKey
  getInboxMessages(offset: number, limit: number): Promise<[object]>
  deleteInboxMessage(id: string): Promise<object>
  setInboxMessageAsClicked(id: string): Promise<object>
  setNavigation(): void;
  setNavigationWithName(screenName: string): void;
  onMessageReceived(params: object): void;
  stopGeofence(): void;
  requestLocationPermissions(): void;
  startGeofence(): void;
  resetAppBadge():void; // android only
  showRealTimeInApp(screenName: string, data: object): void;
  setCity(city: string): void;
  setState(state: string): void;
  setCartAmount(amount: string): void;
  setCartItemCount(count: string): void;
  setCategoryPath(path: string): void;
  setPartnerDeviceId(adid: string): void;
  getLastPushPayload(): Promise<string>;
  registerInAppListener(): void;
  setInAppLinkConfiguration(deeplink: String): void;
  getDeviceId(): Promise<string>;
  setDevelopmentStatus(isDebug: boolean): void;
  setLanguage(language: string): void;
  setDeviceId(deviceId: string): void;


  // NEW
  getSdkVersion(): Promise<string>;
};

DengageRN?.registerNotificationListeners?.()


export * from './InAppInlineView';
export * from './AppStoryView';

export default DengageRN as DengageType;

