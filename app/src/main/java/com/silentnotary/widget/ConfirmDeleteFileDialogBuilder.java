package com.silentnotary.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by albert on 10/9/17.
 */

public class ConfirmDeleteFileDialogBuilder {

    public static void buildAndShow(Context context, DialogInterface.OnClickListener onClickListener){
        new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete the file?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, onClickListener)
                .setNegativeButton(android.R.string.no, null).show();
    }
}
