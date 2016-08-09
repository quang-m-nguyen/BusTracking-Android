package com.thrifa.ruofei.bus_locator.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ruofeixu on 6/6/16.
 */
public class SharedPreferenceUtils {

    private static SharedPreferenceUtils instance;
    private Context context;

    private SharedPreferenceUtils(Context context) {
        this.context = context;

    }

    public static synchronized SharedPreferenceUtils getInstance(final Context context) {
        if (instance == null) {
            instance = new SharedPreferenceUtils(context);
        }
        return instance;
    }

    public void setString(String preferenceKey, String valueKey, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(valueKey, value);
        editor.commit();
    }

    public void setString(int preferenceKey, String valueKey, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(preferenceKey), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(valueKey, value);
        editor.commit();
    }

    public String getString(String preferenceKey, int valueKey, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(valueKey), defaultValue);
    }

    public String getString(int preferenceKey, int valueKey, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(preferenceKey), Context.MODE_PRIVATE);
        return sharedPref.getString(context.getString(valueKey), defaultValue);
    }
//    public Class<?> getValue(String key)
//    {
//
//    }

}
