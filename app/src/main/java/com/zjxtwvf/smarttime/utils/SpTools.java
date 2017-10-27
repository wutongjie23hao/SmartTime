package com.zjxtwvf.smarttime.utils;

/**
 * Created by zjxtwvf on 2017/8/1.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class SpTools {

    public static final String CONFIGFILE = "PASSWD";

    public static void SetBoolean(Context context, String key, Boolean bool) {
        SharedPreferences sp = context.getSharedPreferences(CONFIGFILE, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, bool).commit();
    }

    public static boolean getBoolean(Context context, String key, Boolean bool) {
        SharedPreferences sp = context.getSharedPreferences(CONFIGFILE, Context.MODE_PRIVATE);
        return sp.getBoolean(key, bool);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIGFILE, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIGFILE, Context.MODE_PRIVATE);
        return sp.getString(key, value);
    }

}

