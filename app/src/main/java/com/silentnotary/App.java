package com.silentnotary;

import com.crashlytics.android.Crashlytics;

import com.silentnotary.util.AppLockerManager;

import io.fabric.sdk.android.Fabric;

import android.app.Application;
import android.content.ContextWrapper;

import com.pixplicity.easyprefs.library.Prefs;

import io.realm.Realm;

/**
 * Created by albert on 9/29/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppLockerManager.SetupAppLocker(this);
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        Fabric.with(this, new Crashlytics());
        Realm.init(this);
    }
}
