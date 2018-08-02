package com.silentnotary.ui.auth.register;

import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxFragment;

import com.silentnotary.ui.auth.AuthActivity;
import com.silentnotary.api.requestmanager.AuthApiRequestManager;

import io.reactivex.Observable;

/**
 * Created by albert on 9/29/17.
 */

public class RegisterViewModel {

    AuthApiRequestManager authApiRequestManager = new AuthApiRequestManager();
    private RxFragment rxFragment;

    public RegisterViewModel(RxFragment rxFragment){
        this.rxFragment = rxFragment;
    }

    public void finishAuth() {
        Toast.makeText(rxFragment.getActivity(), "Please check your inbox for a confirmation email.",
                Toast.LENGTH_LONG)
                .show();
        if(rxFragment.getActivity() instanceof AuthActivity){
            ((AuthActivity) rxFragment.getActivity())
                    .loadFragment(AuthActivity.FragmentType.Login);
        }
    }

    public void goToSignIn(){
        if(rxFragment.getActivity() instanceof AuthActivity) {
            AuthActivity authActivity = (AuthActivity) this.rxFragment.getActivity();
            authActivity.loadFragment(AuthActivity.FragmentType.Login);
        }
    }
    Observable<Boolean> signUp(String login, String pass){
        return authApiRequestManager
                .signUp(login, pass)
                .compose(rxFragment.bindToLifecycle());
    }

    public void goToForgetPass() {
        if(rxFragment.getActivity() instanceof AuthActivity) {
            AuthActivity authActivity = (AuthActivity) this.rxFragment.getActivity();
            authActivity.loadFragment(AuthActivity.FragmentType.RestorePassword);
        }
    }
}
