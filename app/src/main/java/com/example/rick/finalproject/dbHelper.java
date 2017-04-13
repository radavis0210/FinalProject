package com.example.rick.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 4/13/2017.
 */

public class dbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "alarmsDatabase";
    private static final String ALARMS_TABLE = "alarmsTable";

    //column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_TIME = "time";

    public dbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + ALARMS_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_TIME + " TEXT);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALARMS_TABLE);
        onCreate(db);
    }

    public void deleteDB(Context context){
        context.deleteDatabase(DB_NAME);
    }

    //Add new alarm
    void addAlarm(Alarm alarm){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, alarm.getName());
        values.put(KEY_DESC, alarm.getDesc());
        values.put(KEY_TIME, alarm.getAlarmTime());

        db.insert(ALARMS_TABLE, null, values);
        db.close();
    }

    // Getting single alarm
    Alarm getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ALARMS_TABLE, new String[] { KEY_ID,
                        KEY_NAME, KEY_DESC, KEY_TIME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
            return  new Alarm(-1, "", "", "");

        Alarm alarm = new Alarm(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        return alarm;
    }

    public List<Alarm> getAllBirds() {
        List<Alarm> alarmList = new ArrayList<Alarm>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ALARMS_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm();
                alarm.setId(Integer.parseInt(cursor.getString(0)));
                alarm.setName(cursor.getString(1));
                alarm.setDesc(cursor.getString(2));
                alarm.setAlarmTime(cursor.getString(3));
                // Adding alarm to list
                alarmList.add(alarm);
            } while (cursor.moveToNext());
        }

        // return alarm list
        return alarmList;
    }

    // Updating specific alarm
    public int updateAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, alarm.getName());
        values.put(KEY_DESC, alarm.getDesc());
        values.put(KEY_TIME, alarm.getAlarmTime());

        // updating row
        return db.update(ALARMS_TABLE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(alarm.getId()) });
    }

    // Deleting single alarm
    public void deleteAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ALARMS_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(alarm.getId()) });
        db.close();
    }

    // Getting alarms Count
    public int getAlarmsCount() {
        String countQuery = "SELECT  * FROM " + ALARMS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

}

