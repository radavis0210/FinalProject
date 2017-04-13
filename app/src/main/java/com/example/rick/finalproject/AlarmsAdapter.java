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
        public TextView name, desc, time;

        public MyViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.Time);
            name = (TextView) view.findViewById(R.id.Name);
            desc = (TextView) view.findViewById(R.id.Desc);

        }
    }


    public AlarmsAdapter(List<Alarm> alarmList) {
        this.alarmList = alarmList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);
        holder.name.setText(alarm.getName());
        holder.desc.setText(alarm.getDesc());
        holder.time.setText(alarm.getAlarmTime());

    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}
