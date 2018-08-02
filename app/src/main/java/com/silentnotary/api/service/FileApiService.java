package com.silentnotary.api.service;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.silentnotary.api.retrofit.FileAPI;
import com.silentnotary.api.retrofit.Request;
import com.silentnotary.api.retrofit.Response;
import com.silentnotary.util.FileUtil;
import com.silentnotary.util.PrefUtil;
import com.silentnotary.util.TimeUtil;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by albert on 9/29/17.
 */

public class FileApiService {

    public MultipartBody.Part getBody(Uri uri) {
        File file = new File(uri.getPath());

        String mimeType = FileUtil.getMimeType(uri);
        MediaType mediaType = null;
        try {
            mediaType = MediaType.parse(mimeType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody requestFile =
                RequestBody.create(
                        mediaType,
                        file
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return body;
    }

    public Observable<FileAPI.UploadFileResponse> create(Uri fileUri, String comment, Context context) {
        return ApiBuilder.buildWithRequiredAuth()
                .create(FileAPI.class)
                .createFile(PrefUtil.getSessionId(), comment, "WebSite",
                        getBody(fileUri));
    }

    public Observable<FileAPI.ConfirmFileResponse> confirmFile(String token) {
        return ApiBuilder.buildInstanceWithoutTimeout()
                .create(FileAPI.class)
                .confirmUploading(new FileAPI.ConfirmFileRequest(token, false));
    }

    public Observable<List<FileAPI.UserFilesResponse>>
    getFiles(Long beginDate, Long endDate) {

        return ApiBuilder.buildWithRequiredAuth()
                .create(FileAPI.class)
                .getFiles(new FileAPI.DatesRequest(TimeUtil.timestampToJsonDate(beginDate),
                        TimeUtil.timestampToJsonDate(endDate)));
    }

    public Observable<FileAPI.UploadFileResponse> createFiles(List<Uri> uriList, String comment, Context context) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri uri : uriList) {
            parts.add(getBody(uri));
        }

        return ApiBuilder.build()
                .create(FileAPI.class)
                .createFiles(PrefUtil.getSessionId(), comment, "WebSite", parts);
    }

    public Observable<Response.RestStatusResponse> deleteFile(String HashHex, String NonceHex) {
        return ApiBuilder.build()
                .create(FileAPI.class)
                .deleteFile(new Request.RestFileRequest(HashHex, NonceHex));
    }

    public Observable<FileAPI.RestVerifyFileResponse> verifyFile(String HashHex, String NonceHex) {
        return ApiBuilder.build()
                .create(FileAPI.class)
                .verifyFile(new Request.RestFileRequest(HashHex, NonceHex));
    }

    public Observable<retrofit2.Response<ResponseBody>> downloadFile(String tokenHex) {
        return Observable.create(s -> {
            ApiBuilder.build()
                    .create(FileAPI.class)
                    .downloadFile(new FileAPI.RestDownloadFileRequest(tokenHex))
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            if (!response.isSuccessful())
                                s.onError(new Exception(response.message()));
                            else
                                s.onNext(response);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            s.onError(t);
                        }
                    });
        });
    }
}
