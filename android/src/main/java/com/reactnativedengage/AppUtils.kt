package com.reactnativedengage

import android.app.NotificationManager
import android.content.Context
import android.os.Build


object AppUtils {

  internal fun resetBadgeCounterOfPushMessages(context: Context) {
    val notificationManager =
      context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (notificationManager != null) {
        notificationManager.cancelAll()
      }
    }
  }
}
