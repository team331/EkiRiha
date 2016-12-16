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

    public void setInteger(String name , int value){
        editor.putInt(name, value);
        editor.commit();
    }

    public int getInteger(String name){
        return sharedPreferences.getInt(name, -1);
    }
    public String getString(String name){
        return sharedPreferences.getString(name, "");
    }

    public void setString(String name, String str){
        editor.putString(name, str);
        editor.commit();
    }
}
