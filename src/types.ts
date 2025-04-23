type NotificationAction = {
  actionIdentifier: string;
  notification: {
    date: string;
    request: {
      identifier: string;
      content: {
        attachments: [Attachment?]
        badge: string | number
        body: string
        categoryIdentifier: string
        launchImageName: string
        sound: Sound
        subtitle: string
        threadIdentifier: string
        title: string
        userInfo?: object
        summaryArgument?: string
        summaryArgumentCount?: number | string
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
  integrationKey: string;
  token?: string | null;
  appVersion?: string | null;
  sdkVersion: string;
  deviceId?: string | null;
  advertisingId: string;
  carrierId: string;
  contactKey?: string | null;
  permission?: boolean | null;
  trackingPermission: boolean;
  tokenType: string;
  webSubscription?: string | null;
  testGroup: string;
  country?: string | null;
  language: string;
  timezone: string;
  partnerDeviceId?: string | null;
  locationPermission?: string | null;
};

export type DengageTypes = {
  NotificationAction: NotificationAction
  Attachment: Attachment
  Sound: Sound
  Trigger: Trigger
  Subscription: Subscription
}
  