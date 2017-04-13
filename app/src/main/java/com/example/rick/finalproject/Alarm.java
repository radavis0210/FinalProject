package com.example.rick.finalproject;

/**
 * Created by Christopher on 4/13/2017.
 */


public class Alarm {

    private int id;
    private String name;
    private String desc;
    //for now alarmTime is left as string, pretty simple to change
    private String alarmTime;
    //audio will probably be implemented here as well, but for now it's left out

    public Alarm(){
        //empty constructor for getAllAlarms in DB
    }

    public Alarm(int id, String name, String desc, String alarmTime){
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.alarmTime = alarmTime;
    }

    public Alarm(String name, String desc, String alarmTime){
        this.name = name;
        this.desc = desc;
        this.alarmTime = alarmTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

}
