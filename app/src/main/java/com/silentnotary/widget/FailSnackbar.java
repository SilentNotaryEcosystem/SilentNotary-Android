package com.silentnotary.widget;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by albert on 9/28/17.
 */

public class FailSnackbar {
    View mRoot;

    public FailSnackbar(View mRoot) {
        this.mRoot = mRoot;
    }

    public void handleError(Throwable throwable) {
        if (mRoot != null) {
            String message = throwable.getMessage();
            if (throwable instanceof java.net.ConnectException) {
                message = "Cannot perform action, please check your internet connection";
            } else if (throwable instanceof java.net.SocketTimeoutException) {
                message = "Connection timeout, it seems some problem with remote server";
            }
            if (message.isEmpty()) {
                message = "Some error occurred. Please try again.";
            }
            try {
                Snackbar.make(mRoot,
                        message, Snackbar.LENGTH_LONG)
                        .show();
            } catch (Exception e) {

            }
        }
    }
}
