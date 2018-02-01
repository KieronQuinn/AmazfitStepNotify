package com.kieronquinn.app.stepnotify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Kieron on 17/01/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("StepNotify", "Boot receiver");
        //Called on boot, reset all alarms and then setup the two alarms for the next hour
        AlarmUtils.cancelAllAlarms(context);
        AlarmUtils.setupAlarmHour(context);
        AlarmUtils.setupAlarmTenTo(context);
    }
}
