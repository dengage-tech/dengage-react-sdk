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

type Subscription = {
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
  