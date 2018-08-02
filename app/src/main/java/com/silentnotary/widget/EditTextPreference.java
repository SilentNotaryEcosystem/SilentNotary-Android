package com.silentnotary.widget;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by albert on 10/4/17.
 */

public class EditTextPreference extends android.preference.EditTextPreference {

    public EditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextPreference(Context context) {
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

    @Override
    protected View onCreateDialogView() {
        return super.onCreateDialogView();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        setSummary(getText());
    }
}