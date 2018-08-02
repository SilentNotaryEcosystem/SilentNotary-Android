package com.silentnotary.ui.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.silentnotary.R;
import com.silentnotary.api.retrofit.UserAPI;
import com.silentnotary.widget.FailSnackbar;
import com.silentnotary.widget.ImageButtonEffect;

/**
 * Created by albert on 10/15/17.
 */

public class UserProfileFragment extends RxFragment {

    @BindView(R.id.buttonSave)
    View saveButton;

    FailSnackbar failSnackbar = null;
    UserProfileViewModel userProfileViewModel = null;

    private void handleSaveUser(UserAPI.IsSuccessResponse isSuccessResponse) {
        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG)
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
                .replace(R.id.contentView, new SettingsFragment())
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userprofile, container, false);
        ButterKnife.bind(this, view);
         failSnackbar = new FailSnackbar(view);
        userProfileViewModel = new UserProfileViewModel(this);
        userProfileViewModel
                .loadUserInfoIntoPrefs()
                .subscribe(s -> showPreference(), err -> err.printStackTrace());
        showPreference();
         saveButton.setOnTouchListener(new ImageButtonEffect(view1 -> {
            saveButton.setEnabled(false);
            userProfileViewModel
                    .saveUserInfo()
                    .subscribe(UserProfileFragment.this::handleSaveUser,
                            UserProfileFragment.this::handleError);
        }, ImageButtonEffect.LIGHT_GRAY_COLOR));
        return view;
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
