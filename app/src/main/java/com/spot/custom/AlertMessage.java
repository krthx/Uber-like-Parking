package com.spot.custom;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;

import com.spot.R;

public class AlertMessage {
    public void showAlertDialog(Context _context, String title, String message, Boolean status) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(_context).create();

        alertDialog.setTitle(title);

        alertDialog.setMessage(message);

        if(status != null )
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }

    public static void showLoader(Context _context, String title, String message, int miliseconds, boolean cancelable) {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(_context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(cancelable);

        alertDialog.show();

        final Handler hdlr = new Handler();

        hdlr.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, miliseconds);
    }

    public static ProgressDialog getAlertDialog(Context _context, String title, String message) {
        final ProgressDialog alertDialog = new ProgressDialog(_context);

        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);

        alertDialog.setMessage(message);

        alertDialog.show();

        return alertDialog;
    }
}
