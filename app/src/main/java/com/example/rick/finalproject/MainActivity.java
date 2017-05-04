package com.example.rick.finalproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    int mHour, mMinute;
    long dif;
    RemindClass reminderService;
    String newAlarmName = "";
    String newAlarmDesc = "";
    long newAlarmTime = 0;
    dbHelper db;


    private List<Alarm> alarmList = new ArrayList<>();
    private RecyclerView rvAlarms;
    private AlarmsAdapter aAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reminderService = new RemindClass();
        final Button b = (Button) findViewById(R.id.btnAdd);
        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                nameAlarm();
            }
        });


        //db/recyclerView setup here... when we have the recycler view (dropdown list thing)
        db = new dbHelper(this);
        rvAlarms = (RecyclerView)findViewById(R.id.rvAlarms);
        aAdapter = new AlarmsAdapter(alarmList);
        RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvAlarms.setLayoutManager(aLayoutManager);
        rvAlarms.setItemAnimator(new DefaultItemAnimator());
        rvAlarms.setAdapter(aAdapter);
        alarmList.addAll(db.getAllAlarms());
        aAdapter.notifyDataSetChanged();

        rvAlarms.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvAlarms, new ClickListener(){
            @Override
            public void onClick(View view, int position){
                Intent i = new Intent(getApplicationContext(), AlarmDisplayActivity.class);
                i.putExtra("Alarm", position+1);
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        if(db.getAlarmsCount() > 0){
            getNextAlarm();
        }

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
            newAlarmTime = timeInMills;
            Alarm newAlarm = new Alarm(newAlarmName, newAlarmDesc, newAlarmTime);
            alarmList.add(newAlarm);
            db.addAlarm(newAlarm);
            aAdapter.notifyDataSetChanged();
            dif = timeInMills - nowL;

            getNextAlarm();
            //Intent intent = new Intent(getApplicationContext(), RemindClass.class);
            //PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
            //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + dif, pendingIntent);
        }
    };

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public void nameAlarm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Alarm Name: ");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newAlarmName = input.getText().toString();
                describeAlarm();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void describeAlarm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Alarm Description: ");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newAlarmDesc = input.getText().toString();
                mHour=0;
                mMinute=0;
                TimePickerDialog timeDialog = new TimePickerDialog(MainActivity.this, mTimeSetListener, mHour, mMinute, false);
                timeDialog.show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

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

