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