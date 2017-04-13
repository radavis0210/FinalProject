package com.example.rick.finalproject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Rick on 4/11/2017.
 */
    public class RemindClass extends IntentService {
//

        public RemindClass(){
            super("RemindClass");
        }
        @Override
        protected void onHandleIntent(Intent workIntent) {

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification.Builder builder = new Notification.Builder(this);

            builder.setContentIntent(contentIntent)
                    //.setSmallIcon()
                    .setTicker("Local Notification Ticker")
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("Local Notification")
                    .setContentText("This is content text.");
            Notification n = builder.getNotification();

            nm.notify(1, n);

        }
    }

