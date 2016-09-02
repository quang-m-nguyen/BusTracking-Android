package com.thrifa.ruofei.bus_locator.service;

import android.app.IntentService;
import android.app.TimePickerDialog;
import android.content.Intent;

import java.sql.Time;

/**
 * Created by ruofeixu on 8/22/16.
 */

public class AlarmService extends IntentService {

//    Time t = new Time();
    public AlarmService(){
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


    }
}
