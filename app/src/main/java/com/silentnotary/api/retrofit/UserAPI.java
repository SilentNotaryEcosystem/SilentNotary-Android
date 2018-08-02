package com.silentnotary.api.retrofit;

import com.silentnotary.api.NetworkingConstantsApi;
import com.silentnotary.model.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by albert on 10/3/17.
 */

public interface UserAPI {

    @POST(NetworkingConstantsApi.GET_USER_DATA)
    Observable<User> getUserProfile();

    @POST(NetworkingConstantsApi.EDIT_PROFILE)
    Observable<IsSuccessResponse> editProfile(@Body EditProfileRequest editProfileRequest);

    class IsSuccessResponse {
        boolean IsSuccess;

        public boolean isSuccess() {
            return IsSuccess;
        }

        public void setSuccess(boolean success) {
            IsSuccess = success;
        }
    }

    class EditProfileRequest {
        public EditProfileRequest() {
        }

        public EditProfileRequest(String email, String firstName, String lastName) {
            Email = email;
            FirstName = firstName;
            LastName = lastName;
        }

        String Email;
        String FirstName;
        String LastName;

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String firstName) {
            FirstName = firstName;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String lastName) {
            LastName = lastName;
        }
    }
}
