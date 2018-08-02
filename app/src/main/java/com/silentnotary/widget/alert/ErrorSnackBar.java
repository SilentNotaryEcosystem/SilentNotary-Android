package com.silentnotary.widget.alert;

import android.content.Context;

import com.silentnotary.R;

/**
 * Created by albert on 10/9/17.
 */

public class ErrorSnackBar extends FailSnackBar {
    public static final int REQUEST_ID = 912;
    private final String title;

    public ErrorSnackBar(Context ctx, String title) {
        super(ctx);
        this.title = title;
        setSetting(new Setting()
                .setAlertText(String.format(title))
        );

    }

}
