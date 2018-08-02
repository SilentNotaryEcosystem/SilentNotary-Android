package com.silentnotary.widget.alert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.silentnotary.R;

/**
 * Created by albert on 9/28/17.
 */

public abstract class AbstractAlert {
    private Context context;

    AbstractAlert(Context context){

        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    abstract void showAlert();
    AlertDialog build(String title, String message, DialogInterface.OnClickListener onClickListener){
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, onClickListener)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
