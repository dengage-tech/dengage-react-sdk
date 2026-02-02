import { NativeModules, Platform } from 'react-native';
import type { DengageTypes, InboxMessage, Subscription, Cart, SdkParameters } from './types';

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

type DengageType = {
  setIntegrationKey(key: string): void; // iOS only
  getIntegrationKey(): Promise<string>; // iOS only
  setFirebaseIntegrationKey(key: string): void; // android only

  promptForPushNotificationsWitCallback(
    callback: (hasPermission: boolean) => void
  ): void; // iOS only
  registerForRemoteNotifications(enable: boolean): void; // iOS only
  getUserPermission(): Promise<boolean>; // android only, in iOS use promptForPushNotificationsWitCallback
  getToken(): Promise<string>;
  setToken(token: String): void;
  setLogStatus(isVisible: boolean): void;
  handleNotificationActionBlock(
    callback: (notificationAction: DengageTypes['NotificationAction']) => void
  ): void; // iOS only,
  
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
  sendCustomEvent(eventTable: string, key: string, parameters: object): void;
  
  setNavigation(): void;
  setNavigationWithName(screenName: string): void;
  onMessageReceived(params: object): void;

  resetAppBadge(): void; // android only
  
  setPartnerDeviceId(adid: string): void;
  getLastPushPayload(): Promise<string>;
  registerInAppListener(): void;
  setInAppLinkConfiguration(deeplink: String): void;
  getDeviceId(): Promise<string>;
  setDevelopmentStatus(isDebug: boolean): void;
  setLanguage(language: string): void;
  setDeviceId(deviceId: string): void;



  
  promptForPushNotifications(): void; // iOS only
  setContactKey(key: string | null): void;
  getContactKey(): Promise<string | null>; // iOS only
  setUserPermission(permission: boolean): void;  
  getSubscription(): Promise<Subscription>; // android only yet. for iOS use getContactKey

  setNavigation(screenName: string | null): void;
  setInAppDeviceInfo(key: string, value: string): void;
  clearInAppDeviceInfo(): void;
  getInAppDeviceInfo(): Promise<Record<string, string>>;
  setCategoryPath(path: string): void;
  setCartItemCount(count: string): void;
  setCartAmount(amount: string): void;
  setState(state: string): void;
  setCity(city: string): void;
  showRealTimeInApp(screenName: string, params: Record<string, string>): void;
  
  setCart(cart: Cart): Promise<boolean>;
  getCart(): Promise<Cart>;

  getInboxMessages(offset: number, limit: number): Promise<[InboxMessage]>;
  deleteInboxMessage(id: string): Promise<boolean>;
  setInboxMessageAsClicked(id: string): Promise<boolean>;
  deleteAllInboxMessages(): Promise<boolean>;
  setAllInboxMessageAsClicked(): Promise<boolean>;

  requestLocationPermissions(): void;
  startGeofence(): void;
  stopGeofence(): void;
  
  getSdkParameters(): Promise<SdkParameters | null>;

  // NEW
  getSdkVersion(): Promise<string>;


  // deprecated
  registerNotificationListeners(): void; // End Developer No Need to call this, we're calling it ourself, to register for events like `onNotificationReceived` & `onNotificationClicked`
};

DengageRN?.registerNotificationListeners?.();

export * from './InAppInlineView';
export * from './StoriesListView';
export * from './types';


export default DengageRN as DengageType;
