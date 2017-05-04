package com.example.rick.finalproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Christopher on 5/3/2017.
 */

public class AlarmDisplayActivity extends AppCompatActivity {

    EditText etName;
    EditText etDesc;
    TextView tvTime;
    Button btnUpdate;
    Button btnDelete;
    Button btnBack;
    long time;
    Alarm curAlarm;
    dbHelper db;
    int mHour, mMinute;
    long dif;
    RemindClass reminderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_display);

        etName = (EditText)findViewById(R.id.etName);
        etDesc = (EditText)findViewById(R.id.etDesc);
        tvTime = (TextView)findViewById(R.id.tvTime);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnBack = (Button)findViewById(R.id.btnBack);

        btnUpdate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                curAlarm.setName(etName.getText().toString());
                curAlarm.setDesc(etDesc.getText().toString());
                db.updateAlarm(curAlarm);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                db.deleteAlarm(curAlarm);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("Alarm");

        db = new dbHelper(this);
        curAlarm = db.getAlarm(id);

        etName.setText(curAlarm.getName());
        etDesc.setText(curAlarm.getDesc());
        long milliseconds = curAlarm.getAlarmTime();
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        String ampm = "am";
        String extra = "";
        if(hours > 12){
            ampm = "pm";
            hours -= 12;
        }
        if(hours == 0){
            hours = 12;
        }
        if(minutes < 10){
            extra = "0";
        }

        tvTime.setText(hours + ":" + extra + minutes + ampm);

        tvTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mHour=0;
                mMinute=0;
                TimePickerDialog timeDialog = new TimePickerDialog(AlarmDisplayActivity.this, mTimeSetListener, mHour, mMinute, false);
                timeDialog.show();
            }
        });

    }

    TimePickerDialog.OnTimeSetListener mTimeSetListener =  new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker v, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
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
            curAlarm.setAlarmTime(timeInMills);
            long milliseconds = curAlarm.getAlarmTime();
            int minutes = (int) ((milliseconds / (1000*60)) % 60);
            int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
            String ampm = "am";
            String extra = "";
            if(hours > 12){
                ampm = "pm";
                hours -= 12;
            }
            if(hours == 0){
                hours = 12;
            }
            if(minutes < 10){
                extra = "0";
            }

            tvTime.setText(hours + ":" + extra + minutes + ampm);
            dif = timeInMills - nowL;

            getNextAlarm();
        }
    };

    public void getNextAlarm(){
        int alarmCount = db.getAlarmsCount();
        if(alarmCount > 0){
            long lowest = Long.MAX_VALUE;
            for(int i = 1; i <= alarmCount; i++){
                Alarm temp = db.getAlarm(i);
                Date now = new Date(System.currentTimeMillis());
                long nowL = ((now.getHours() * 60 + now.getMinutes()) * 60 + now.getSeconds()) * 1000;
                if((temp.getAlarmTime()-nowL < lowest) && (temp.getAlarmTime()-nowL > 0)){
                    lowest = temp.getAlarmTime()-nowL;
                }
            }
            if(lowest == Long.MAX_VALUE){
                for(int i = 1; i <= alarmCount; i++){
                    Alarm temp = db.getAlarm(i);
                    if(temp.getAlarmTime() < lowest){
                        lowest = temp.getAlarmTime();
                    }
                }
                Date now = new Date(System.currentTimeMillis());
                long nowL = ((now.getHours() * 60 + now.getMinutes()) * 60 + now.getSeconds()) * 1000;
                lowest += (86400000-nowL);
            }
            Intent newIntent = new Intent(getApplicationContext(), RemindClass.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, newIntent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + lowest, pendingIntent);
        }
    }
}
