package com.kieronquinn.app.stepnotify.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.kieronquinn.app.stepnotify.AlarmUtils;
import com.kieronquinn.app.stepnotify.R;

import java.util.ArrayList;
import java.util.List;

public class TimesActivity extends AppCompatActivity {

    /*
        Activity to provide a settings list for choosing hours
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView root = new RecyclerView(this);
        //Add header to a list of settings
        List<BaseSetting> settings = new ArrayList<>();
        final SharedPreferences sharedPreferences = getSharedPreferences(getPackageName()+"_settings", Context.MODE_PRIVATE);
        settings.add(new HeaderSetting(getString(R.string.set_times)));
        //Setup items for each hour
        for(int x = 0; x < 24; x++){
            //Each item needs a SwitchSetting with a value
            final int finalX = x;
            settings.add(new SwitchSetting(null, String.valueOf(x) + ":50", null, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    sharedPreferences.edit().putBoolean("time_"+ finalX, b).commit();
                }
            }, getSetting(x, sharedPreferences)));
        }
        //Setup layout
        root.setLayoutManager(new LinearLayoutManager(this));
        root.setAdapter(new Adapter(this, settings));
        root.setPadding((int) getResources().getDimension(R.dimen.padding_round_small), 0, (int) getResources().getDimension(R.dimen.padding_round_small), (int) getResources().getDimension(R.dimen.padding_round_large));
        root.setClipToPadding(false);
        setContentView(root);
    }

    private boolean getSetting(int x, SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("time_"+x, AlarmUtils.getDefaultHour(x));
    }
}
