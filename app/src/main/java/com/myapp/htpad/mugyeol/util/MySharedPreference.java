package com.myapp.htpad.mugyeol.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {

    private static MySharedPreference INSTANCE = null;

    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;


    private MySharedPreference(Context context){
        mSharedPreferences = context.getSharedPreferences("autologin",Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.apply();
    }
    public static MySharedPreference getInstance(Context context){
        if (INSTANCE ==null){
            INSTANCE = new MySharedPreference(context);
        }
        return INSTANCE;
    }
    public void putBoolean (String key, boolean value){
        mEditor.putBoolean(key,value);
        mEditor.apply();
    }
    public void putString (String key, String value){
        mEditor.putString(key,value);
        mEditor.apply();
    }
    public boolean getBoolean (String key,boolean def){
       return mSharedPreferences.getBoolean(key,def);
    }
    public String getString (String key){
       return mSharedPreferences.getString(key,"");
    }
    public void removeToken (){
     mEditor.clear();
     mEditor.apply();
    }


}
