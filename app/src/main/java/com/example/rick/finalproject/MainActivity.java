package com.example.rick.finalproject;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    int mHour, mMinute;
    RemindClass reminderService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reminderService = new RemindClass("RemindClass");

        TimePickerDialog dialog = new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
        dialog.show();
    }

    TimePickerDialog.OnTimeSetListener mTimeSetListener =  new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker v, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, Calendar.YEAR);
            c.set(Calendar.MONTH, Calendar.MONTH);
            c.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH);
            c.set(Calendar.HOUR_OF_DAY, mHour);
            c.set(Calendar.MINUTE, mMinute);
            c.set(Calendar.SECOND, 0);

            long timeInMills = c.getTimeInMillis();

            Intent intent = new Intent(MainActivity.this, RemindClass.class);
            PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
            alarmManager.set(AlarmManager.RTC, timeInMills, pendingIntent);
        }
    };

}

