//package com.example.reactnativedengage;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.media.AudioAttributes;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.RemoteViews;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import com.dengage.sdk.Constants;
//import com.dengage.sdk.NotificationReceiver;
//import com.dengage.sdk.Utils;
//import com.dengage.sdk.models.CarouselItem;
//import com.dengage.sdk.models.Message;
//
//import java.util.List;
//import java.util.UUID;
//
//public class MyReceiver extends NotificationReceiver {
//  String channelId = null;
//  @Override
//  protected void onCarouselRender(Context context, Intent intent, Message message) {
//    // accessing to carousel items with its own images.
//    CarouselItem[] items = message.getCarouselContent();
//
//    // show first image
//    Bitmap img = Utils.loadImageFromStorage(items[0].getMediaFileLocation(), items[0].getMediaFileName());
//    String itemTitle = items[0].getTitle();
//    String itemDesc = items[0].getDescription();
//
//    // set intents (right button, left button, item click)
//    Intent itemIntent = getItemClickIntent(intent.getExtras(), context.getPackageName());
//    Intent leftIntent = getLeftItemIntent(intent.getExtras(), context.getPackageName());
//    Intent rightIntent = getRightItemIntent(intent.getExtras(), context.getPackageName());
//    Intent deleteIntent = getDeleteIntent(intent.getExtras(), context.getPackageName());
//    Intent contentIntent = getContentIntent(intent.getExtras(), context.getPackageName());
//    PendingIntent carouseItemIntent = PendingIntent.getBroadcast(context, 0,
//      itemIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    PendingIntent carouselLeftIntent = PendingIntent.getBroadcast(context, 1,
//      leftIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    PendingIntent carouselRightIntent = PendingIntent.getBroadcast(context, 2,
//      rightIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, 4,
//      deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    PendingIntent contentPendingIntent = PendingIntent.getBroadcast(context, 5,
//      contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//    // set views for layout. please notice that we use den_carousel_landscape.xml file.
//    RemoteViews collapsedView = new RemoteViews(context.getPackageName(), 		R.layout.den_carousel_collapsed);
//    collapsedView.setTextViewText(R.id.den_carousel_title, message.getTitle());
//    collapsedView.setTextViewText(R.id.den_carousel_message, message.getMessage());
//    RemoteViews carouselView = new RemoteViews(context.getPackageName(), R.layout.den_carousel_landscape);
//    carouselView.setTextViewText(R.id.den_carousel_title, message.getTitle());
//    carouselView.setTextViewText(R.id.den_carousel_message, message.getMessage());
//    carouselView.setTextViewText(R.id.den_carousel_item_title, itemTitle);
//    carouselView.setTextViewText(R.id.den_carousel_item_description, itemDesc);
//    Utils.loadCarouselImageToView(
//      carouselView,
//      R.id.den_carousel_landscape_image,
//      items[0]
//    );
//    carouselView.setOnClickPendingIntent(R.id.den_carousel_landscape_image, carouseItemIntent);
//    carouselView.setOnClickPendingIntent(R.id.den_carousel_item_title, carouseItemIntent);
//    carouselView.setOnClickPendingIntent(R.id.den_carousel_item_description, carouseItemIntent);
//    carouselView.setOnClickPendingIntent(R.id.den_carousel_left_image, carouselLeftIntent);
//    carouselView.setOnClickPendingIntent(R.id.den_carousel_right_image, carouselRightIntent);
//
//    Uri soundUri = Utils.getSound(context, message.getSound());
//    // generate new channel id for different sounds
//    channelId = UUID.randomUUID().toString();
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//      NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//      // delete old notification channels
//      List<NotificationChannel> channels = notificationManager.getNotificationChannels();
//      if (channels != null && channels.size() > 0) {
//        for (NotificationChannel channel : channels) {
//          notificationManager.deleteNotificationChannel(channel.getId());
//        }
//      }
//      NotificationChannel notificationChannel = new NotificationChannel(
//        channelId,
//        Constants.CHANNEL_NAME,
//        NotificationManager.IMPORTANCE_DEFAULT
//      );
//      AudioAttributes audioAttributes = new AudioAttributes.Builder()
//        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//        .build();
//      notificationChannel.setSound(soundUri, audioAttributes);
//      notificationManager.createNotificationChannel(notificationChannel);
//    }
//
//    // build notification with the layout.
//    Notification notification = new NotificationCompat.Builder(context, channelId)
//      .setSmallIcon(R.mipmap.ic_launcher)
//      .setCustomContentView(collapsedView)
//      .setCustomBigContentView(carouselView)
//      .setContentIntent(contentPendingIntent)
//      .setDeleteIntent(deletePendingIntent)
//      .build();
//
//    // show notification
//    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//    notificationManager.notify(message.getMessageSource(), message.getMessageId(), notification);
//  }
//
//  @Override
//  protected void onCarouselReRender(Context context, Intent intent, Message message) {
//    // accessing the carousel items with their own images.
//    CarouselItem[] items = message.getCarouselContent();
//    // find current item to show prev/next items.
//    Bundle bundle = intent.getExtras();
//    int size = message.getCarouselContent().length;
//    String navigation = bundle.getString("navigation", "right");
//    int current = bundle.getInt("current", 0);
//    int newIndex = 0;
//    if (navigation.equals("right")) {
//      newIndex = (current + 1) % size;
//    } else {
//      newIndex = (current - 1 + size) % size;
//    }
//    intent.putExtra("current", newIndex);
//
//    // accessing the current item.
//    Bitmap img = Utils.loadImageFromStorage(items[newIndex].getMediaFileLocation(), items[newIndex].getMediaFileName());
//    String itemTitle = items[newIndex].getTitle();
//    String itemDesc = items[newIndex].getDescription();
//
//    // set intents (right/left button, item click)
//    Intent itemIntent = getItemClickIntent(intent.getExtras(), context.getPackageName());
//    Intent leftIntent = getLeftItemIntent(intent.getExtras(), context.getPackageName());
//    Intent rightIntent = getRightItemIntent(intent.getExtras(), context.getPackageName());
//    Intent deleteIntent = getDeleteIntent(intent.getExtras(), context.getPackageName());
//    Intent contentIntent = getContentIntent(intent.getExtras(), context.getPackageName());
//    PendingIntent carouseItemIntent = PendingIntent.getBroadcast(context, 0,
//      itemIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    PendingIntent carouselLeftIntent = PendingIntent.getBroadcast(context, 1,
//      leftIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    PendingIntent carouselRightIntent = PendingIntent.getBroadcast(context, 2,
//      rightIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    PendingIntent deletePendingIntent = PendingIntent.getBroadcast(context, 4,
//      deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    PendingIntent contentPendingIntent = PendingIntent.getBroadcast(context, 5,
//      contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//    // set views for the layout
//    RemoteViews collapsedView = new RemoteViews(context.getPackageName(), R.layout.den_carousel_collapsed);
//    collapsedView.setTextViewText(R.id.den_carousel_title, message.getTitle());
//    collapsedView.setTextViewText(R.id.den_carousel_message, message.getMessage());
//    RemoteViews carouselView = new RemoteViews(context.getPackageName(), R.layout.den_carousel_landscape);
//    carouselView.setTextViewText(R.id.den_carousel_title, message.getTitle());
//    carouselView.setTextViewText(R.id.den_carousel_message, message.getMessage());
//    carouselView.setTextViewText(R.id.den_carousel_item_title, itemTitle);
//    carouselView.setTextViewText(R.id.den_carousel_item_description, itemDesc);
//    Utils.loadCarouselImageToView(
//      carouselView,
//      R.id.den_carousel_landscape_image,
//      items[current]
//    );
//    carouselView.setOnClickPendingIntent(R.id.den_carousel_landscape_image, carouseItemIntent);
//    carouselView.setOnClickPendingIntent(R.id.den_carousel_item_title, carouseItemIntent);
//    carouselView.setOnClickPendingIntent(R.id.den_carousel_item_description, carouseItemIntent);
//    carouselView.setOnClickPendingIntent(R.id.den_carousel_left_image, carouselLeftIntent);
//    carouselView.setOnClickPendingIntent(R.id.den_carousel_right_image, carouselRightIntent);
//
//    // create channel
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && channelId == null) {
//      channelId = UUID.randomUUID().toString();
//      NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//      NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
//      notificationManager.createNotificationChannel(notificationChannel);
//    }
//
//    // build notification with the layout.
//    Notification notification = new NotificationCompat.Builder(context, channelId)
//      .setSmallIcon(R.mipmap.ic_launcher)
//      .setCustomContentView(collapsedView)
//      .setCustomBigContentView(carouselView)
//      .setContentIntent(contentPendingIntent)
//      .setDeleteIntent(deletePendingIntent)
//      .build();
//
//    // show notification silently with curent item.
//    notification.flags |= Notification.FLAG_AUTO_CANCEL;
//    notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
//    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//    notificationManager.notify(message.getMessageSource(), message.getMessageId(), notification);
//  }
//
//  @Override
//  protected void onActionClick(Context context, Intent intent) {
//    Bundle extras = intent.getExtras();
//    if(extras != null)
//    {
//      String actionId = extras.getString("id");
//      int notificationId = extras.getInt("notificationId");
//      String targetUrl = extras.getString("targetUrl");
//
//      Log.d("DenPush", actionId +" is clicked");
//    }
//
//    // Remove if you prefer to handle targetUrl which is actually correspond a deeplink.
//    super.onActionClick(context, intent);
//  }
//}
