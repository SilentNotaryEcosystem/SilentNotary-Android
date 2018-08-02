package com.silentnotary.api.service;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.silentnotary.ui.auth.AuthActivity;
import com.silentnotary.api.requestmanager.AuthApiRequestManager;
import com.silentnotary.util.PrefUtil;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by albert on 9/29/17.
 */

public class ApiBuilder {

    private static final String BASE_URL = "https://api.silentnotary.io/";
    private static final String TAG = "API_BUILDER";
    private static Retrofit instance = null;
    private static Retrofit instanceWithRequredAuth = null;
    private static Retrofit instanceWithoutTimeout = null;
    private static Activity activity;

    public static void setActivity(Activity activity) {
        ApiBuilder.activity = activity;
    }

    public static void showLoginActivity() {
        try {
            activity.runOnUiThread(() -> {
                activity.startActivityForResult(new Intent(activity, AuthActivity.class), -1);
            });

        } catch (Exception e) {
        }
    }

    static String bodyToString(final RequestBody request) {
        final Buffer buffer = new Buffer();
        try {
            final RequestBody copy = request;

            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "";
        } finally {
            buffer.close();
        }
    }

    private static void setSessionId(RequestBody body, Request.Builder builder, String sessionId) {
        try {
            String postBodyString = bodyToString(body);
            postBodyString = postBodyString.isEmpty() ? "{}" : postBodyString;
            JSONObject jsonObject = new JSONObject(postBodyString);
            jsonObject.put("SessionId", sessionId);
            builder.post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
                    jsonObject.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Retrofit createRetrofitInstance(boolean requiredAuth, int timeout) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor authInterceptor = chain -> {
            Request.Builder builder = chain.request().newBuilder();
            setSessionId(chain.request().body(), builder, PrefUtil.getSessionId());
            Request request = builder
                    .addHeader("Content-Type", "application/json").build();
            Response response = chain.proceed(request);
            if (response.code() == 401) {
                PrefUtil.LoginCredentials loginCredentials = PrefUtil.getLoginCredentials();
                if (loginCredentials.getField1().isEmpty()
                        || loginCredentials.getField2().isEmpty()) {
                    showLoginActivity();
                } else {
                    try {
                        String sessionId = new AuthApiRequestManager()
                                .signIn(loginCredentials.getField2(), loginCredentials.getField1())
                                .blockingFirst();
                        PrefUtil.setSessionId(sessionId);
                        builder = chain.request().newBuilder();
                        setSessionId(chain.request().body(), builder, PrefUtil.getSessionId());
                        request = builder
                                .addHeader("Content-Type", "application/json").build();
                        response = chain.proceed(request);
                    } catch (Exception e) {
                        showLoginActivity();
                    }
                }
            }
            return response;
        };


        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };


        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.interceptors().add(httpLoggingInterceptor);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        client.connectTimeout(timeout, TimeUnit.SECONDS);
        if (requiredAuth)
            client.interceptors().add(authInterceptor);
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client.build())
                .build();
    }

    public static Retrofit build() {
        if (instance == null) {
            instance = createRetrofitInstance(false, 20);
        }
        return instance;
    }

    public static Retrofit buildWithRequiredAuth() {
        if (instanceWithRequredAuth == null) {
            instanceWithRequredAuth = createRetrofitInstance(true, 20);
        }
        return instanceWithRequredAuth;
    }

    public static Retrofit buildInstanceWithoutTimeout() {
        if (instanceWithoutTimeout == null) {
            instanceWithoutTimeout = createRetrofitInstance(false, 120);
        }
        return instanceWithoutTimeout;
    }
}
