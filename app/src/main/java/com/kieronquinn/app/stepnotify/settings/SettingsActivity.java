package com.kieronquinn.app.stepnotify.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.kieronquinn.app.stepnotify.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    /*
        Activity to provide a settings list for choosing vibration and sub-settings
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView root = new RecyclerView(this);
        final SharedPreferences sharedPreferences = getSharedPreferences(getPackageName()+"_settings", Context.MODE_PRIVATE);
        //Add header to a list of settings
        List<BaseSetting> settings = new ArrayList<>();
        settings.add(new HeaderSetting(getString(R.string.settings)));
        //Add IconSettings for each sub-setting. They contain an icon, title and subtitle, as well as a click action to launch the sub-setting's activity
        settings.add(new IconSetting(getDrawable(R.drawable.clock), getString(R.string.set_times), getString(R.string.set_times_c), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, TimesActivity.class));
            }
        }));
        settings.add(new IconSetting(getDrawable(R.drawable.calendar), getString(R.string.set_days), getString(R.string.set_days_c), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, DaysActivity.class));
            }
        }));
        //Add vibration switch setting
        settings.add(new SwitchSetting(null, getString(R.string.vibrate), getString(R.string.vibrate_c), new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPreferences.edit().putBoolean("vibrate", b).commit();
            }
        }, sharedPreferences.getBoolean("vibrate", true)));
        //Setup layout
        root.setLayoutManager(new LinearLayoutManager(this));
        root.setAdapter(new Adapter(this, settings));
        root.setPadding((int) getResources().getDimension(R.dimen.padding_round_small), 0, (int) getResources().getDimension(R.dimen.padding_round_small), (int) getResources().getDimension(R.dimen.padding_round_large));
        root.setClipToPadding(false);
        setContentView(root);
    }
}
