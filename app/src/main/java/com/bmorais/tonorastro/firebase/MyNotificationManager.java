package com.bmorais.tonorastro.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


import com.bmorais.tonorastro.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by Bruno Morais2 on 11/08/2017.
 */

public class MyNotificationManager {

    public static int ID_NOTIFICATION = 234;
    public static String ID_CHANNEL = "Default";

    private Context mCtx;

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    @SuppressLint("UnspecifiedImmutableFlag")
    public void showNotification(String title, String message, String urlImagem, Intent intent) {

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
        String dateString = sdf.format(date);

        ID_NOTIFICATION = Integer.parseInt(dateString);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            resultPendingIntent = PendingIntent.getActivity(mCtx, ID_NOTIFICATION, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            resultPendingIntent = PendingIntent.getActivity
                    (mCtx, ID_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, ID_CHANNEL);
        Notification notification;

        mBuilder.setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentText(message)
                .setColor(ContextCompat.getColor(mCtx, R.color.colorPrimary));

        if (urlImagem.length()>0) {
            mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(urlImagem)).bigLargeIcon(null));
            mBuilder.setLargeIcon(getBitmapFromURL(urlImagem));
        } else {
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.logo));
        }

        notification = mBuilder.build();

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel("Padrão","Todas as notificações", notificationManager);

        notificationManager.notify(ID_NOTIFICATION, notification);
    }

    //The method will return Bitmap from an image URL
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createNotificationChannel(String channel_name, String channel_description, NotificationManager notificationManager) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ID_CHANNEL, channel_name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(channel_description);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }
}