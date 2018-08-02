package com.silentnotary.ui.setting;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.trello.rxlifecycle2.components.RxFragment;

import java.lang.reflect.Field;

import com.silentnotary.api.exception.CannotEditUserException;
import com.silentnotary.api.requestmanager.UserApiRequestManager;
import com.silentnotary.api.retrofit.UserAPI;
import com.silentnotary.model.User;
import com.silentnotary.util.FileUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by albert on 10/4/17.
 */

public class UserProfileViewModel {
    private RxFragment rxFragment;
    private UserApiRequestManager requestManager = new UserApiRequestManager();

    public UserProfileViewModel(RxFragment activity) {
        this.rxFragment = activity;
    }

    Observable<UserAPI.IsSuccessResponse> saveUserInfo() {
        return Observable.<User>create(s -> {
            User user = new User();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(rxFragment.getActivity());
            user.setEmail(sharedPref.getString("Email", ""));
            user.setFirstName(sharedPref.getString("FirstName", ""));
            user.setLastName(sharedPref.getString("LastName", ""));
            s.onNext(user);
            s.onComplete();
        })
                .flatMap(user -> requestManager.editProfile(user))
                .map(isSuccessResponse -> {
                    if (!isSuccessResponse.isSuccess())
                        throw new CannotEditUserException();
                    return isSuccessResponse;
                })
                .compose(rxFragment.bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<User> loadUser(User user) {
        return Observable.create(s -> {
            if (user == null) return;
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(rxFragment.getActivity());
            SharedPreferences.Editor editor = sharedPref.edit();

            boolean changed = false;
            for (Field field : user.getClass().getDeclaredFields()) {
                String name = field.getName();
                field.setAccessible(true); // You might want to set modifier to public first.
                try {
                    Object value = field.get(user);
                    String stringValue = String.valueOf(value == null ? "" : value);
                    if (name.equals("Balance") || name.equals("TotalMonthlyFee")) {
                        stringValue = stringValue + "$";
                    } else if (name.equals("TotalFileSizeBytes")) {
                        stringValue = FileUtil.readableFileSize((Long) value);
                    }

                    if (!sharedPref.getString(name, "").equals(stringValue)) {
                        editor.putString(name, stringValue);
                        changed = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            editor.apply();
            if (changed)
                s.onNext(user);

            s.onComplete();
        });
    }

    public Observable<User> loadUserInfoIntoPrefs() {
        return requestManager
                .getUserProfile()
                .flatMap(this::loadUser)
                .compose(rxFragment.bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
