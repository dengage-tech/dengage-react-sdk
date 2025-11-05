type NotificationAction = {
  actionIdentifier: string;
  notification: {
    date: string;
    request: {
      identifier: string;
      content: {
        attachments: [Attachment?]
        badge: string
        body: string
        categoryIdentifier: string
        launchImageName: string
        sound: Sound
        subtitle: string
        threadIdentifier: string
        title: string
        userInfo?: object
        summaryArgument?: string
        summaryArgumentCount?: number
        targetContentIdentifier?: string
      }
      trigger?: Trigger
    }
  }
}

type Sound = {

}

type Trigger = {
  repeats: boolean
}

type Attachment = {
  identifier: string;
  url: string;
  type: string;
}

export type Subscription = {
  integrationKey?: string;
  token?: string;
  appVersion?: string;
  sdkVersion: string;
  deviceId?: string;
  advertisingId: string;
  carrierId: string;
  contactKey?: string;
  permission?: boolean;
  trackingPermission: boolean;
  tokenType: string;
  webSubscription?: string;
  testGroup: string;
  country?: string;
  language: string;
  timezone: string;
  partnerDeviceId?: string;
  locationPermission?: string;
};

export type DengageTypes = {
  NotificationAction: NotificationAction
  Attachment: Attachment
  Sound: Sound
  Trigger: Trigger
  Subscription: Subscription
}

export type CarouselItem = {
  id: string;
  title: string;
  descriptionText: string;
  mediaUrl: string;
  targetUrl: string;
};

export type InboxMessage = {
  id: string;
  title?: string;
  message?: string;
  mediaURL?: string;
  targetUrl?: string;
  receiveDate?: string;
  isClicked: boolean;
  carouselItems?: CarouselItem[];
  [key: string]: any;
};

export type CartItem = {
  productId: string;
  productVariantId: string;
  categoryPath: string;
  price: number;
  discountedPrice: number;
  hasDiscount: boolean;
  hasPromotion: boolean;
  quantity: number;
  attributes: Record<string, string>;
  // Calculated fields
  effectivePrice: number;
  lineTotal: number;
  discountedLineTotal: number;
  effectiveLineTotal: number;
  // Normalized fields
  categorySegments: string[];
  categoryRoot: string;
};

export type CartSummary = {
  currency: string;
  updatedAt: number; // timestamp
  // Count fields
  linesCount: number;
  itemsCount: number;
  // Total fields
  subtotal: number;
  discountedSubtotal: number;
  effectiveSubtotal: number;
  // Discount flags
  anyDiscounted: boolean;
  allDiscounted: boolean;
  // Price ranges
  minPrice: number;
  maxPrice: number;
  minEffectivePrice: number;
  maxEffectivePrice: number;
  // Category breakdown
  categories: Record<string, number>;
};

export type Cart = {
  items: CartItem[];
  summary: CartSummary;
};

export type SdkParameters = {
  appId?: string;
  accountId?: number;
  accountName?: string;
  eventsEnabled: boolean;
  inboxEnabled?: boolean;
  inAppEnabled?: boolean;
  subscriptionEnabled?: boolean;
  inAppFetchIntervalInMin?: number;
  expiredMessagesFetchIntervalInMin?: number;
  inAppMinSecBetweenMessages?: number;
  lastFetchTimeInMillis?: number;
  appTrackingEnabled?: boolean;
  appTrackingList?: AppTracking[];
  realTimeInAppEnabled?: boolean;
  realTimeInAppFetchIntervalInMinutes?: number;
  realTimeInAppSessionTimeoutMinutes?: number;
  surveyCheckEndpoint?: string;
  eventMappings?: EventMapping[];
  debugDeviceIds?: string[];
};

export type AppTracking = {
  [key: string]: any;
};

export type EventMapping = {
  eventTableName?: string;
  eventTypeDefinitions?: EventTypeDefinition[];
};

export type EventTypeDefinition = {
  eventTypeId?: number;
  eventType?: string;
  logicOperator?: string;
  filterConditions?: FilterCondition[];
  enableClientHistory?: boolean;
  clientHistoryOptions?: ClientHistoryOptions;
  attributes?: EventAttribute[];
};

export type FilterCondition = {
  fieldName?: string;
  operator?: string;
  values?: string[];
};

export type ClientHistoryOptions = {
  maxEventCount?: number;
  timeWindowInMinutes?: number;
};

export type EventAttribute = {
  name?: string;
  dataType?: string;
  tableColumnName?: string;
};