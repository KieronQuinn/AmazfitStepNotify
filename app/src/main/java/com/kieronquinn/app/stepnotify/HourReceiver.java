package com.kieronquinn.app.stepnotify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Kieron on 17/01/2018.
 */

public class HourReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //Log.d("StepNotify", "HourReceiver onReceive " + Calendar.getInstance().getTimeInMillis());
        //Add the next hour alarm
        AlarmUtils.setupAlarmHour(context);
        //Check if the current time is enabled, and if not, return
        if(!AlarmUtils.isValidTime(context))return;
        //Get the sensor service and then the step sensor (type 19)
        final SensorManager mManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor mStepSensor = mManager.getDefaultSensor(19);
        //Create listener
        SensorEventListener mListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor arg1, int arg2) {
            }

            public void onSensorChanged(SensorEvent arg8) {
                //Unregister listener to prevent repeated running
                mManager.unregisterListener(this);
                //Get the step count. This is copied from the decompiled Amazfit Health app
                float[] v1 = arg8.values;
                if (v1 != null && v1.length >= 1) {
                    int v0 = ((int) v1[0]);
                    if (v0 < 0) {
                        //Log.i("StepNotify", "count is below zero");
                    } else {
                        //Log.d("StepNotify", "HourReceiver Steps " + v0);
                        //Place step count into the preferences for use at 10 to
                        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName() + "_steps", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putInt("steps", v0).commit();
                    }
                }
            }
        };
        //Start listening (instant callback)
        mManager.registerListener(mListener, mStepSensor, 0);
    }
}
