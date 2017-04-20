package com.example.rick.finalproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int mHour, mMinute;
    RemindClass reminderService;

    private List<Alarm> alarmList = new ArrayList<>();
    private RecyclerView rvAlarms;
    private AlarmsAdapter aAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reminderService = new RemindClass();

        TimePickerDialog dialog = new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);
        dialog.show();

        //db/recyclerView setup here... when we have the recycler view (dropdown list thing)
        dbHelper db = new dbHelper(this);

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


            Date now = new Date(System.currentTimeMillis());
            long nowL = ((now.getHours() * 60 + now.getMinutes()) * 60 + now.getSeconds()) * 1000;


            long timeInMills = (long) (mHour * 60 + mMinute) * 60000;

            long dif = timeInMills - nowL;

            Intent intent = new Intent(getApplicationContext(), RemindClass.class);
            startService(intent);
            PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);

            alarmManager.set(AlarmManager.RTC, dif, pendingIntent);

        }
    };

}

