package com.example.rick.finalproject;

import android.app.AlarmManager;
import android.app.ApplicationErrorReport;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import java.util.Date;

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



            Intent intent = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            if(!(plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB)){
                builder.setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.icon)
                        .setTicker("Local Notification Ticker")
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle("Local Notification")
                        .setContentText("Plugged in.");
                Notification n = builder.build();
                nm.notify(1, n);
            }

            dbHelper db = new dbHelper(this);
            int alarmCount = db.getAlarmsCount();
            if(alarmCount > 0){
                long lowest = Long.MAX_VALUE;
                for(int i = 0; i < alarmCount; i++){
                    Alarm temp = db.getAlarm(i);
                    Date now = new Date(System.currentTimeMillis());
                    long nowL = ((now.getHours() * 60 + now.getMinutes()) * 60 + now.getSeconds()) * 1000;
                    if((temp.getAlarmTime()-nowL < lowest) && (temp.getAlarmTime()-nowL > 0)){
                        lowest = temp.getAlarmTime()-nowL;
                    }
                }
                Intent newIntent = new Intent(getApplicationContext(), RemindClass.class);
                PendingIntent pendingIntent = PendingIntent.getService(this, 0, newIntent, 0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + lowest, pendingIntent);
            }


        }
    }

