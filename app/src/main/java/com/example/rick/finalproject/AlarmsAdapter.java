/**
 * AlarmsAdapter Class is used
 * in order to inflate the items
 * for the Alarms RecyclerView
 * DropDown List
 */
package com.example.rick.finalproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Christopher on 4/13/2017.
 */

public class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.MyViewHolder> {

    private List<Alarm> alarmList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, desc;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tvName);
            desc = (TextView) view.findViewById(R.id.tvDesc);

        }
    }


    public AlarmsAdapter(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);
        long milliseconds = alarm.getAlarmTime();
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

        holder.name.setText(alarm.getName() + " " + hours + ":" + extra + minutes + ampm);
        holder.desc.setText(alarm.getDesc());

    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}
