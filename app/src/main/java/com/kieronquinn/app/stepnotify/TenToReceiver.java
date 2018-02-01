package com.kieronquinn.app.stepnotify;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Kieron on 17/01/2018.
 */

public class TenToReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //Log.d("StepNotify", "TenToReceiver onReceive " + Calendar.getInstance().getTimeInMillis());
        //Add the next 10 to alarm
        AlarmUtils.setupAlarmTenTo(context);
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
                        //Log.i("StepNotify", "TenToReceiver count is below zero");
                    } else {
                        //Log.d("StepNotify", "TenToReceiver checkSteps");
                        //Check the steps against the stored steps
                        checkSteps(v0, context);
                    }
                }
            }
        };
        //Start listening (instant callback)
        mManager.registerListener(mListener, mStepSensor, 0);
    }

    private void checkSteps(int newSteps, Context context) {
        //Load steps from preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+"_steps", Context.MODE_PRIVATE);
        int steps = sharedPreferences.getInt("steps", -1);
        //Not yet run / data cleared
        if(steps != -1){
            //Get current steps and calculate difference
            int difference = newSteps - steps;
            //Log.d("StepNotify", "TenToReceiver Steps " + steps);
            //Log.d("StepNotify", "TenToReceiver newSteps " + newSteps);
            //Log.d("StepNotify", "TenToReceiver Difference " + difference);
            //Check if difference needs notifying for
            if(difference > -1 && difference < 250){
                //send notification
                sendNotification(difference, context);
            }
        }
    }

    private void sendNotification(int difference, Context context){
        //Get prefs to check vibration
        final SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+"_settings", Context.MODE_PRIVATE);
        //Log.d("StepNotify", "TenToReceiver Sending Notification");
        //Setup content based on the difference
        String content = difference == 0 ? context.getString(R.string.notification_content_zero) : context.getString(R.string.notification_content, difference);
        //Create notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.steps)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(content);
        //Setup vibration
        if(sharedPreferences.getBoolean("vibrate", true))mBuilder.setVibrate(new long[] { 1000, 1000});
        //Send notification
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
