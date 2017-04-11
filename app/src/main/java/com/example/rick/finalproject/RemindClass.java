package com.example.rick.finalproject;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Rick on 4/11/2017.
 */
    public class RemindClass extends IntentService {

        public RemindClass(){
            super("ReminderService");
        }
        @Override
        protected void onHandleIntent(Intent workIntent) {
            // Gets data from the incoming Intent
            String dataString = workIntent.getDataString();

            // Do work here, based on the contents of dataString

        }
    }

