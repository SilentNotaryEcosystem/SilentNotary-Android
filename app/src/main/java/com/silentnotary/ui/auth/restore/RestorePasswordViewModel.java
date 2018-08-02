package com.silentnotary.ui.auth.restore;

import com.trello.rxlifecycle2.components.support.RxFragment;

import com.silentnotary.ui.auth.AuthActivity;
import com.silentnotary.api.exception.CannotRestorePasswordException;
import com.silentnotary.api.requestmanager.AuthApiRequestManager;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by albert on 10/10/17.
 */

public class RestorePasswordViewModel {

    private AuthApiRequestManager requestManager;

    private RxFragment rxFragment;

    public RestorePasswordViewModel(RxFragment rxFragment){
        this.rxFragment = rxFragment;
        requestManager = new AuthApiRequestManager();
    }
    void backToLogin(){
        if(rxFragment.getActivity() instanceof AuthActivity) {
            AuthActivity authActivity = (AuthActivity) this.rxFragment.getActivity();
            authActivity.loadFragment(AuthActivity.FragmentType.Login);
        }
    }

    Completable restorePassword(String email){
        return Completable.create(s -> requestManager.restorePassword(email)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxFragment.bindToLifecycle())
                .subscribe(res -> {
                    if(res.isSuccess())
                        s.onComplete();
                    else
                        throw new CannotRestorePasswordException();
                }, s::onError));
    }
}
