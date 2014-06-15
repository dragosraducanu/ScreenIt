package com.dragos.screenit.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *Created by Raducanu Dragos (raducanu.dragos@gmail.com) on 15.06.2014.
 */
public class NetworkUtils {



    public static boolean hasInternetConnection(Context context){
        NetworkInfo netInfo = getNetworkInfo(context);
        return (netInfo != null && netInfo.isConnected() && netInfo.getType() != ConnectivityManager.TYPE_BLUETOOTH);
    }

    public static boolean isRoamingConnected(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return hasInternetConnection(context) && networkInfo.isRoaming();
    }

    public static boolean isWifiConnected(Context context){
        NetworkInfo networkInfo = getNetworkInfo(context);
        return hasInternetConnection(context) && (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_WIMAX);
    }

    public static boolean isMobileConnected(Context context){
        NetworkInfo networkInfo = getNetworkInfo(context);
        return hasInternetConnection(context) && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    private static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean allowConnection(Context context){
        //check if the settings and device state allow connecting to the server
        if(isMobileConnected(context) && !PreferencesUtils.getAllowMobile(context)) {
            return false;
        }
        if(isWifiConnected(context) && !PreferencesUtils.getAllowWiFi(context)){
            return false;
        }
        if(isRoamingConnected(context) && !PreferencesUtils.getAllowRoaming(context)) {
            return false;
        }
        return true;
    }


}
