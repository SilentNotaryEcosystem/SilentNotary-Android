package com.silentnotary.ui.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.silentnotary.R;
import com.silentnotary.api.retrofit.UserAPI;
import com.silentnotary.widget.FailSnackbar;
import com.silentnotary.widget.ImageButtonEffect;

/**
 * Created by albert on 9/29/17.
 */

public class SettingActivity extends RxAppCompatActivity {

    @BindView(R.id.buttonSave)
    View saveButton;

    FailSnackbar failSnackbar = null;

    UserProfileViewModel userProfileViewModel = null;

    private void handleSaveUser(UserAPI.IsSuccessResponse isSuccessResponse) {
        Toast.makeText(this, "Saved", Toast.LENGTH_LONG)
                .show();
        saveButton.setEnabled(true);
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof Exception) {
            failSnackbar.handleError(new Exception("Cannot save user info"));
        }
        saveButton.setEnabled(true);
    }

    void showPreference() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        failSnackbar = new FailSnackbar(findViewById(R.id.content));

        showPreference();
        ButterKnife.bind(this);
        saveButton.setOnTouchListener(new ImageButtonEffect(view -> {
            saveButton.setEnabled(false);
            userProfileViewModel
                    .saveUserInfo()
                    .subscribe(SettingActivity.this::handleSaveUser,
                            SettingActivity.this::handleError);
        }, ImageButtonEffect.LIGHT_GRAY_COLOR));
    }


    public static class SettingsFragment extends PreferenceFragment {
        public SettingsFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_main);
        }
    }
}
