package com.dragos.screenit.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * Created by Dragos Raducanu (raducanu.dragos@gmail.com) on 15.06.2014.
 */
public class PreferencesUtils {
    public static boolean getAllowRoaming(Context context){
        SharedPreferences prefs = getSharedPrefs(context);
        return prefs.getBoolean("allow_roaming", false);
    }
    public static boolean getAllowWiFi(Context context){
        SharedPreferences prefs = getSharedPrefs(context);
        Set<String> stringSet = prefs.getStringSet("connection_type", null);
        if(stringSet == null) {
            return true;
        }
        return stringSet.contains("1");
    }
    public static boolean getAllowMobile(Context context){
        SharedPreferences prefs = getSharedPrefs(context);
        Set<String> stringSet = prefs.getStringSet("connection_type", null);
        if(stringSet == null) {
            return true;
        }
        return stringSet.contains("2");
    }
    public static boolean getOptimizeImages(Context context){
        SharedPreferences prefs = getSharedPrefs(context);
        return prefs.getBoolean("optimize_upload", false);
    }

    public static SharedPreferences getSharedPrefs(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
