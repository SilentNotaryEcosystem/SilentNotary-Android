package com.silentnotary.widget.alert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;

import com.silentnotary.R;

/**
 * Created by albert on 9/28/17.
 */

public class SnackbarNeedPermission extends FailSnackBar {

    public static final int REQUEST_ID = 912;
    private final String title;

    public SnackbarNeedPermission(Context ctx, String title) {
        super(ctx);
        this.title = title;
        setSetting(new Setting()
                    .setAlertText(String.format("Please allow %s permission", title))
                    .setActionButton(snackbar -> {
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + getContext().getPackageName()));
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (getContext() instanceof Activity) {
                                ((Activity) getContext()).startActivityForResult(intent, REQUEST_ID);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }, ctx.getString(R.string.add))
        );

    }

}
