package com.silentnotary.widget.alert;

import android.app.Activity;
import android.content.Context;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import com.silentnotary.R;

/**
 * Created by albert on 8/28/17.
 */

public class FailSnackBar extends AbstractAlert {
    Snackbar snack = null;
    private Setting setting = new Setting();


    public FailSnackBar(Context ctx) {
        super(ctx);
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    @Override
    public void showAlert() {
        try {
            hideSnack();

            snack = Snackbar.with(getContext()).position(getSetting().position)
                    .text(getSetting().getAlertText())
                    .duration(setting.getDuration());
            if (getSetting().actionbutton) {
                snack.actionLabel(getSetting().actiontext);
                snack.actionColor(getContext().getResources().getColor(R.color.white));
                snack.actionListener(getSetting().listener);
            }

            if (getSetting().enabledAlpha)
                snack.backgroundDrawable(R.color.red_withc_alpha);
            else
                snack.backgroundDrawable(R.color.red);

            SnackbarManager.show(snack);
        } catch (Exception e) {

        }
    }

    public void hideSnack() {

        if (getSnack() != null
                && getContext() instanceof Activity
                &&  ((Activity) getContext()) != null) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getSnack().dismiss();
                }
            });
        } else {
            try {
                if (getSnack() != null)
                    getSnack().dismiss();
            } catch (Exception e) {

            }
        }

    }

    public Snackbar getSnack() {
        return snack;
    }

    public static class Setting {
        private boolean actionbutton;
        private ActionClickListener listener = null;
        private String actiontext = "";
        private String alertText = "";
        private Snackbar.SnackbarDuration duration = Snackbar.SnackbarDuration.LENGTH_LONG;
        private Snackbar.SnackbarPosition position = Snackbar.SnackbarPosition.TOP;
        private boolean enabledAlpha = true;

        public Snackbar.SnackbarDuration getDuration() {
            return duration;
        }

        public Setting setDuration(Snackbar.SnackbarDuration duration) {
            this.duration = duration;
            return this;
        }

        public Setting setActionButton(ActionClickListener listener, String text) {
            this.listener = listener;
            this.actiontext = text;
            this.actionbutton = true;
            return this;
        }

        public Setting setEnabledAlpha(boolean enabledAlpha) {
            this.enabledAlpha = enabledAlpha;
            return this;
        }

        public Setting setPosition(Snackbar.SnackbarPosition position) {
            this.position = position;
            return this;
        }

        public String getAlertText() {
            return alertText;
        }

        public Setting setAlertText(String alertText) {
            this.alertText = alertText;
            return this;
        }
    }
}
