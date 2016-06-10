package com.example.ruofei.bus_locator.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ruofei.bus_locator.R;

/**
 * Created by ruofeixu on 6/6/16.
 */
public class SharedPreferenceUtils {

    private static SharedPreferenceUtils instance;
    private Context context;

    private SharedPreferenceUtils(Context context){
        this.context = context;

    }

    public static synchronized SharedPreferenceUtils getInstance(final Context context) {
        if (instance == null) {
            instance = new SharedPreferenceUtils(context);
        }
        return instance;
    }

    public void setString(String preferenceName, String key, String value)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

//    public Class<?> getValue(String key)
//    {
//
//    }

}
