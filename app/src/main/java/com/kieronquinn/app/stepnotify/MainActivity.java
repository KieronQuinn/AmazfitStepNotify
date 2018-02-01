package com.kieronquinn.app.stepnotify;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kieronquinn.app.stepnotify.settings.SettingsActivity;

/**
 * Created by Kieron on 17/01/2018.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup activity
        setContentView(R.layout.activity_main);
        //Log.d("StepNotify", "HourReceiver Cancelling alarms");
        //Reset all alarms
        AlarmUtils.cancelAllAlarms(this);
        //Log.d("StepNotify", "HourReceiver BaseSetting up");
        //Setup alarms for next hour
        AlarmUtils.setupAlarmHour(this);
        AlarmUtils.setupAlarmTenTo(this);
    }

    public void close(View view) {
        //Simply exit without killing
        finish();
    }

    public void settings(View view) {
        //Start settings activity
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
