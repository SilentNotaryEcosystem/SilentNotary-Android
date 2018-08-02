package com.silentnotary.api.requestmanager;

import com.silentnotary.api.retrofit.UserAPI;
import com.silentnotary.api.service.UserApiService;
import com.silentnotary.model.User;

import io.reactivex.Observable;

/**
 * Created by albert on 10/3/17.
 */

public class UserApiRequestManager {

    UserApiService userApiService = new UserApiService();

    public Observable<User> getUserProfile() {
        return userApiService
                .getUserProfile();
    }

    public Observable<UserAPI.IsSuccessResponse> editProfile(User user) {
        return userApiService.editProfile(new UserAPI.EditProfileRequest(user.getEmail(),
                user.getFirstName(), user.getLastName()));
    }
}
