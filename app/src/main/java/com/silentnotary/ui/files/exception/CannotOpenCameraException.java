package com.silentnotary.ui.files.exception;

import android.content.Context;

import com.silentnotary.R;

/**
 * Created by albert on 9/28/17.
 */

public class CannotOpenCameraException extends Exception {

    public CannotOpenCameraException(Context ctx) {
        super(ctx.getString(R.string.cannot_open_camera));
    }
}
