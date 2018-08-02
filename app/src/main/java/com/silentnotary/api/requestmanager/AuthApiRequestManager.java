package com.silentnotary.api.requestmanager;

import com.silentnotary.api.exception.CannotRegisterUserException;
import com.silentnotary.api.exception.InvalidLoginOrPassException;
import com.silentnotary.api.retrofit.UserAPI;
import com.silentnotary.api.service.AuthApiService;

import io.reactivex.Observable;

/**
 * Created by albert on 9/29/17.
 */

public class AuthApiRequestManager {

    AuthApiService authApiService = new AuthApiService();

    public Observable<String> signIn(String login, String pass) {
        return authApiService
                .login(login, pass)
                .map(res -> {
                    if (res.getSessionId() == null || res.getSessionId().isEmpty()) {
                        throw new InvalidLoginOrPassException();
                    } else {
                        return res.getSessionId();
                    }
                });
    }

    public Observable<String> signUpAndSignIn(String login, String pass) {
        return authApiService
                .login(login, pass)
                .flatMap(res -> {
                    if (res.getSessionId() == null || res.getSessionId().isEmpty()) {
                        return authApiService.register(login, pass)
                                .map(r -> {
                                    if (!r.isSuccess()) {
                                        throw new CannotRegisterUserException();
                                    }
                                    return null;
                                })
                                .flatMap(e -> this.signIn(login, pass));
                    } else {
                        return Observable.just(res.getSessionId());
                    }
                });

    }

    public Observable<Boolean> signUp(String login, String pass) {
        return authApiService.register(login, pass)
                .map(r -> {
                    if (!r.isSuccess()) {
                        throw new CannotRegisterUserException();
                    }
                    return true;
                });
    }

    public Observable<UserAPI.IsSuccessResponse> restorePassword(String email) {
        return authApiService.restorePassword(email);
    }

}
