package com.silentnotary.api.service;

import com.silentnotary.api.retrofit.AuthAPI;
import com.silentnotary.api.retrofit.UserAPI;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by albert on 9/29/17.
 */

public class AuthApiService {

    AuthAPI getAuthApi(){
        return ApiBuilder.build()
                .create(AuthAPI.class);
    }
    public Observable<AuthAPI.LoginResponse> login(String login, String pass){
        return getAuthApi()
                .login(new AuthAPI.AuthRequest(login, pass))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<AuthAPI.RegisterResponse> register(String login, String pass){
        return getAuthApi()
                .register(new AuthAPI.AuthRequest(login, pass))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<UserAPI.IsSuccessResponse> restorePassword(String email){
        return getAuthApi()
                .restorePassword(new AuthAPI.RestorePasswordRequest(email));
    }
}
