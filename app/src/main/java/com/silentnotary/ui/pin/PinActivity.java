package com.silentnotary.ui.pin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.github.orangegangsters.lollipin.lib.managers.AppLockActivity;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;

import com.silentnotary.ui.auth.AuthActivity;
import com.silentnotary.ui.main.view.BaseActivity;
import com.silentnotary.util.PrefUtil;

/**
 * Created by albert on 10/25/17.
 */

public class PinActivity extends AppLockActivity {

    @Override
    public void showForgotDialog() {
        finish();
        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
        intent.putExtra(AuthActivity.Extra.ON_BACK_PRESSES_FINISH_APP, true);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent(AppLockActivity.ACTION_CANCEL));
        startActivity(intent);
    }

    @Override
    public void onPinFailure(int attempts) {
    }

    @Override
    public void onPinSuccess(int attempts) {
    }

    @Override
    public int getPinLength() {
        return 4;//you can override this method to change the pin length from the default 4
    }
}
