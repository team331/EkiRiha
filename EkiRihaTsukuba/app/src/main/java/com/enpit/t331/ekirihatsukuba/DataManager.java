package com.enpit.t331.ekirihatsukuba;

import android.content.SharedPreferences;

/**
 * Created by woo on 16/11/9.
 */
public class DataManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    DataManager(SharedPreferences sp){
        sharedPreferences = sp;
        editor = sharedPreferences.edit();
    }

    public boolean getBoolean(String name, boolean default_value){
        return sharedPreferences.getBoolean(name, default_value);
    }

    public void setBoolean(String name, boolean value){
        editor.putBoolean(name, value);
        editor.commit();
    }
}
