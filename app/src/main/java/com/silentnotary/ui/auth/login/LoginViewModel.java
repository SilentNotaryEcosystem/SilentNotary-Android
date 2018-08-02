package com.silentnotary.ui.auth.login;

import com.trello.rxlifecycle2.components.support.RxFragment;

import com.silentnotary.ui.auth.AuthActivity;
import com.silentnotary.api.requestmanager.AuthApiRequestManager;
import com.silentnotary.util.PrefUtil;
import io.reactivex.Observable;

/**
 * Created by albert on 9/29/17.
 */

public class LoginViewModel {
    private RxFragment rxFragment;

    LoginViewModel(RxFragment context){
        this.rxFragment = context;
    }

    AuthApiRequestManager authApiRequestManager = new AuthApiRequestManager();
    Observable<String> signIn(String login, String pass){
        return authApiRequestManager
                .signIn(login, pass)
                .compose(rxFragment.bindToLifecycle());
    }

    public void goToSignup() {
        if(rxFragment.getActivity() instanceof AuthActivity) {
            AuthActivity authActivity = (AuthActivity) this.rxFragment.getActivity();
            authActivity.loadFragment(AuthActivity.FragmentType.SignUp);
        }
    }

    public void finishAuth(String sessionId, String email) {
        PrefUtil.setSessionId(sessionId);
        PrefUtil.setUserEmail(email);

        if(rxFragment.getActivity() instanceof AuthActivity){
            ((AuthActivity) rxFragment.getActivity())
                    .finishAuth();
        }
    }

    public void goToForgetPass() {
        if(rxFragment.getActivity() instanceof AuthActivity) {
            AuthActivity authActivity = (AuthActivity) this.rxFragment.getActivity();
            authActivity.loadFragment(AuthActivity.FragmentType.RestorePassword);
        }
    }
}
