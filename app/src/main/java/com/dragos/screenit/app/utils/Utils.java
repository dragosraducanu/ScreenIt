package com.dragos.screenit.app.utils;

import android.app.Activity;
import android.view.View;

import com.dragos.screenit.app.ScreenitApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by dragos on 02.07.2014.
 */
public class Utils {

    public static void sendScreenView(Activity activity, String screenName) {
        Tracker t = ((ScreenitApplication) activity.getApplication()).getTracker(ScreenitApplication.TrackerName.APP_TRACKER);
        t.setScreenName(screenName);
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    public static void sendEvent(Activity activity, String category, String action, long value) {

        // Get tracker.
        Tracker t = ((ScreenitApplication) activity.getApplication()).getTracker(ScreenitApplication.TrackerName.APP_TRACKER);

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setValue(value)
                .build());
    }
}
