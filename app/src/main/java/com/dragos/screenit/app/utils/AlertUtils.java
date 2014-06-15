package com.dragos.screenit.app.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.dragos.screenit.app.R;
import com.dragos.screenit.app.activities.SettingsActivity;

/**
 *Created by Raducanu Dragos (raducanu.dragos@gmail.com) on 02.06.2014.
 */
public class AlertUtils {

    public static void showErrorDialog(Context context, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.error_title));
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showBlockedSettingsDialog(final Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.warning_title));
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent settingsIntent = new Intent(context, SettingsActivity.class);
                context.startActivity(settingsIntent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
