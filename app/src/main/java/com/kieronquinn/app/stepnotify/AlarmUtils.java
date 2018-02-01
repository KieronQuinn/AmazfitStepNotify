package com.kieronquinn.app.stepnotify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Kieron on 17/01/2018.
 */

public class AlarmUtils {

    public static void setupAlarmHour(Context context){
        //Log.d("StepNotify", "setupAlarmHour");
        //Create an alarm for the next whole hour to run the HourReceiver
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, HourReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Date date = Calendar.getInstance().getTime();
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, roundToNextWholeHour(date).getTime(), alarmIntent);
    }

    public static void setupAlarmTenTo(Context context){
        //Log.d("StepNotify", "setupAlarmTenTo");
        //Create an alarm for the next ten to the hour to run the TenToReceiver
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TenToReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Date date = Calendar.getInstance().getTime();
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, roundToNextTenTo(date).getTime(), alarmIntent);
    }

    public static void cancelAllAlarms(Context context){
        //Log.d("StepNotify", "cancelAllAlarms");
        //Cancel both alarms by recreating their PendingIntents and canceling them
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent1 = new Intent(context, HourReceiver.class);
        PendingIntent alarmIntent1 = PendingIntent.getBroadcast(context, 0, intent1, 0);
        alarmMgr.cancel(alarmIntent1);
        Intent intent2 = new Intent(context, TenToReceiver.class);
        PendingIntent alarmIntent2 = PendingIntent.getBroadcast(context, 0, intent2, 0);
        alarmMgr.cancel(alarmIntent2);
    }

    private static Date roundToNextWholeHour(Date date) {
        //Add 1h and reset the minutes to 0 for the current time, and return
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(Calendar.HOUR, 1);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        //Log.d("StepNotify", "roundToNextWholeHour " + c.getTime().getTime());
        return c.getTime();
    }

    private static Date roundToNextTenTo(Date date) {
        //Add 1h and set the minutes to 50 for the current time, and return
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(Calendar.HOUR, 1);
        c.set(Calendar.MINUTE, 50);
        c.set(Calendar.SECOND, 0);
        //Log.d("StepNotify", "roundToNextTenTo " + c.getTime().getTime());
        return c.getTime();
    }

    public static boolean getDefaultHour(int x) {
        //Default hours are between 9 and 5
        return x > 8 && x < 18;
    }

    public static boolean isValidTime(Context context){
        //Check if the current time is valid against the saved days and times from the preferences. Default is all days enabled and the use of getDefaultHour
        Calendar c = Calendar.getInstance();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName()+"_settings", Context.MODE_PRIVATE);
        boolean isValidDay = sharedPreferences.getBoolean("day_" + (c.get(Calendar.DAY_OF_WEEK) - 1), true);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        boolean isValidHour = sharedPreferences.getBoolean("time_" + hour, getDefaultHour(hour));
        return isValidDay && isValidHour;
    }
}
