package com.silentnotary.util;

import android.content.Context;

import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.AppLockImpl;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;

import com.silentnotary.R;
import com.silentnotary.ui.LaunchActivity;
import com.silentnotary.ui.auth.AuthActivity;
import com.silentnotary.ui.main.view.BaseActivity;
import com.silentnotary.ui.pin.PinActivity;

/**
 * Created by albert on 10/25/17.
 */

public class AppLockerManager {
    public static void SetupAppLocker(Context context) {
        LockManager<PinActivity> lockManager = LockManager.getInstance();
        lockManager.enableAppLock(context, PinActivity.class);
        AppLock appLock = lockManager.getAppLock();
        appLock.setLogoId(R.drawable.silentnotary_logo_splash);
        appLock.addIgnoredActivity(AuthActivity.class);
        appLock.addIgnoredActivity(LaunchActivity.class);
        BaseActivity.setListener(AppLockImpl.getInstance(context, PinActivity.class));
        appLock.setTimeout(10000);
    }
}
