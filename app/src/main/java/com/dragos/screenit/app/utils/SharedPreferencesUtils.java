package com.dragos.screenit.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *Created by Raducanu Dragos (raducanu.dragos@gmail.com) on 27.05.2014.
 */
public class SharedPreferencesUtils {
    public static String FIRST_TIME_LAUNCH = "first_time_launch";

    /**
     * checks if it's the first time the app is being launched.
     * @param context  Context.
     * @return if it's the first time the app is being launched. Default is true.
     */
    public static boolean isFirstLaunch(Context context){
        SharedPreferences prefs = context.getSharedPreferences("com.dragos.screenit.app", Context.MODE_PRIVATE);
        return prefs.getBoolean(FIRST_TIME_LAUNCH, true);
    }

    /**
     * saves the FIRST_TIME_LAUNCH, value to SharedPrefs
     * @param context - Context
     * @param value - boolean value
     */
    public static void setFirstTimeLaunch(Context context, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences("com.dragos.screenit.app", Context.MODE_PRIVATE);
        prefs.edit().putBoolean(FIRST_TIME_LAUNCH, value).commit();
    }
}
