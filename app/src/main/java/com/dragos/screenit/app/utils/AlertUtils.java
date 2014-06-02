package com.dragos.screenit.app.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.dragos.screenit.app.R;

/**
 * Created by dragos on 02.06.2014.
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
}
