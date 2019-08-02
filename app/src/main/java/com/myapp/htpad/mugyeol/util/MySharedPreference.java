package com.myapp.htpad.mugyeol.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * shared preference -> singleton
 * **/
public class MySharedPreference {

    private static MySharedPreference INSTANCE = null;

    private SharedPreferences mSharedPreferences;


    private MySharedPreference(Context context){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static MySharedPreference getInstance(Context context){
        if (INSTANCE ==null){
            INSTANCE = new MySharedPreference(context);
        }
        return INSTANCE;
    }


    public void putBoolean (String key, boolean value){
        mSharedPreferences.edit().putBoolean(key,value).apply();
    }
    public void putString (String key, String value){
        mSharedPreferences.edit().putString(key,value).apply();
    }
    public boolean getBoolean (String key,boolean def){
       return mSharedPreferences.getBoolean(key,def);
    }
    public String getString (String key){
       return mSharedPreferences.getString(key,"");
    }
    public void removeToken (){
        mSharedPreferences.edit().clear().apply();
    }


}
