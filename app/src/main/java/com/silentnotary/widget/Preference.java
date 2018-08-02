package com.silentnotary.widget;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

/**
 * Created by albert on 10/4/17.
 */

public class Preference extends android.preference.Preference {

    public Preference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Preference(Context context) {
        super(context);
    }

    @Override
    public CharSequence getSummary() {
        return super.getSummary() == null ? "" : super.getSummary();
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        super.onSetInitialValue(restoreValue, defaultValue);
        setSummary(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getKey(), getSummary().toString()));
    }
}
