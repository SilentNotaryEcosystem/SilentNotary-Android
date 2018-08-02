package com.silentnotary.api.retrofit;

import com.silentnotary.api.NetworkingConstantsApi;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by albert on 9/29/17.
 */

public interface AuthAPI {

    @POST(NetworkingConstantsApi.USER_LOGIN_ENDPOINT)
    Observable<LoginResponse> login(@Body AuthRequest loginRequest);

    @POST(NetworkingConstantsApi.USER_REGISTRATION_ENDPOINT)
    Observable<RegisterResponse> register(@Body AuthRequest loginRequest);

    @POST(NetworkingConstantsApi.USER_RESTORE_PASSWORD)
    Observable<UserAPI.IsSuccessResponse> restorePassword(@Body RestorePasswordRequest request);


    class RestorePasswordRequest {
        String Email;

        public RestorePasswordRequest(String email) {
            Email = email;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }
    }

    class RegisterResponse {
        boolean IsSuccess;

        public boolean isSuccess() {
            return IsSuccess;
        }

        public void setSuccess(boolean success) {
            IsSuccess = success;
        }
    }

    class LoginResponse {
        String SessionId;

        public String getSessionId() {
            return SessionId;
        }

        public void setSessionId(String sessionId) {
            SessionId = sessionId;
        }
    }

    class AuthRequest {
        String Login;
        String Password;

        public AuthRequest() {
        }

        public AuthRequest(String login, String password) {
            Login = login;
            Password = password;
        }

        public String getLogin() {
            return Login;
        }

        public void setLogin(String login) {
            Login = login;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            Password = password;
        }
    }
}
