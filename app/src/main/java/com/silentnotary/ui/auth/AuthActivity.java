package com.silentnotary.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.silentnotary.R;
import com.silentnotary.ui.FirstActivity;
import com.silentnotary.ui.auth.login.LoginFragment;
import com.silentnotary.ui.auth.register.RegisterFrament;
import com.silentnotary.ui.auth.restore.RestorePasswordFragment;
import com.silentnotary.util.PrefUtil;

/**
 * Created by albert on 9/29/17.
 */

public class AuthActivity extends AppCompatActivity {
    public enum FragmentType {
        Login,
        SignUp,
        RestorePassword
    }

    public interface Extra {
        String ON_BACK_PRESSES_FINISH_APP = "com.silentnotary.activity.auth.ON_BACK_PRESSES_FINISH_APP";
    }

    boolean onBackPressedFinishApp = false;

    public void finishAuth() {
        finish();
        LockManager.getInstance().getAppLock().disableAndRemoveConfiguration();
        startActivity(new Intent(this, FirstActivity.class));
    }

    public void loadFragment(FragmentType fragmentType) {
        Fragment fragment = null;
        if (fragmentType == FragmentType.Login) {
            fragment = new LoginFragment();
        } else if (fragmentType == FragmentType.SignUp) {
            fragment = new RegisterFrament();
        } else if (fragmentType == FragmentType.RestorePassword) {
            fragment = new RestorePasswordFragment();
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        this.onBackPressedFinishApp = getIntent().getBooleanExtra(Extra.ON_BACK_PRESSES_FINISH_APP, false);
        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
                    if (fragment instanceof IKeyboardListener) {
                        ((IKeyboardListener) fragment)
                                .onChange(isOpen);
                    }
                });
        this.loadFragment(FragmentType.Login);
    }
}
