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

export type DengageTypes = {
  NotificationAction: NotificationAction
  Attachment: Attachment
  Sound: Sound
  Trigger: Trigger
}
