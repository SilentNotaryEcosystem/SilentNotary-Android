package com.silentnotary.api.service;

import com.silentnotary.api.retrofit.UserAPI;
import com.silentnotary.model.User;
import io.reactivex.Observable;

/**
 * Created by albert on 10/3/17.
 */

public class UserApiService {

    public Observable<User> getUserProfile(){
       return ApiBuilder.buildWithRequiredAuth()
                .create(UserAPI.class)
                .getUserProfile();
    }

    public Observable<UserAPI.IsSuccessResponse> editProfile(UserAPI.EditProfileRequest editProfileRequest){
        return ApiBuilder.buildWithRequiredAuth()
                .create(UserAPI.class)
                .editProfile(editProfileRequest);
    }
}
