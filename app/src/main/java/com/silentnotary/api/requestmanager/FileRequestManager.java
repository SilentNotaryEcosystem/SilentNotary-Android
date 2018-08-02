package com.silentnotary.api.requestmanager;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.silentnotary.api.exception.CannotSaveFileToDownloadFolder;
import com.silentnotary.api.retrofit.FileAPI;
import com.silentnotary.api.retrofit.Response;
import com.silentnotary.api.service.FileApiService;
import com.silentnotary.model.File;
import com.silentnotary.util.FileUtil;
import com.silentnotary.util.TimeUtil;

import io.reactivex.Observable;

/**
 * Created by albert on 9/27/17.
 */

public class FileRequestManager extends CachableRequestManager<List<File>> {

    FileApiService fileApiService = new FileApiService();

    public FileRequestManager(Context context) {
        super(context);
    }

    public Observable<List<File>> getAllFilesFake() {
        Observable<List<File>> observable = Observable.create(s -> {
            File[] files = {new File("hash1", System.currentTimeMillis(), System.currentTimeMillis() + 100000,
                    "Super type", 100, "SuperComment", "0x611d6a835370c552b8899cc94bf5b3b06923395dbf8831247d14b5dc46427448", "nonecode"),
                    new File("hash2", System.currentTimeMillis(), System.currentTimeMillis() + 100000,
                            "Super type", 100500, "SuperComment", "", "nonescode"),
            };
            s.onNext(Arrays.asList(files));
            s.onComplete();
        });
        return observable;
    }

    public Observable<FileAPI.UploadFileResponse> uploadFile(Uri fileUri, String comment, Context context) {
        return fileApiService.create(fileUri, comment, context);
    }

    public Observable<List<File>> getAllFiles() {
        return Observable.create(emitter -> {
            tryToReadFromCache(emitter, new ArrayList<>());
            fileApiService
                    .getFiles(0L, 1598059696000L)
                    .flatMap(Observable::fromIterable)
                    .filter(it -> !it.isDeleted())
                    .map(s -> new File(s.getHashHex(), TimeUtil.parseJsonDate(s.getDateTime()),
                            TimeUtil.parseJsonDate(s.getPayedPeriodEnd()),
                            s.getType(), s.getFileSizeBytes(), s.getComment(), s.getTransaction(), s.getNonceHex()))
                    .toList()
                    .toObservable()
                    .subscribe(files -> {
                        saveObjectToCache(files);
                        emitter.onNext(files);
                    }, err -> {

                    }, emitter::onComplete);

        });
    }

    public Observable<FileAPI.UploadFileResponse> uploadFiles(List<java.io.File> files, String comment, Context context) {
        List<Uri> uris = new ArrayList<>();
        for (java.io.File file : files) {
            uris.add(Uri.fromFile(file));
        }
        return fileApiService
                .createFiles(uris, comment, context);
    }

    public Observable<FileConfirmationWrapper> confirmUploading(String token) {
        return fileApiService
                .confirmFile(token)
                .map(s -> new FileConfirmationWrapper(s, token));
    }

    public Observable<Response.RestStatusResponse> deleteFile(String HashHex, String NonceHex) {
        return fileApiService
                .deleteFile(HashHex, NonceHex);
    }

    public Observable<java.io.File> downloadFile(String HashHex, String NonceHex, String folder) {
        String fileName = "file.zip";
        java.io.File file = FileUtil.getFileFromDownloadFolder(fileName, folder);
        int i = fileName.indexOf(".");
        if (i != -1) {
            fileName = fileName.substring(0, i)
                    + "-" + HashHex.substring(0, 5)
                    + NonceHex.substring(0, 5)
                    + HashHex.substring(HashHex.length() - 5);
        }
        file = FileUtil.getFileFromDownloadFolder(fileName, folder);
        //If already exist
        if (file.exists() && file.length() > 0) {
            return Observable.just(file);
        }
        java.io.File finalFile = file;
        return fileApiService
                .verifyFile(HashHex, NonceHex)
                .flatMap(resp -> fileApiService.downloadFile(resp.getTokenHex()))
                .map(responseBody -> {
                    boolean saved = FileUtil.writeResponseBodyToDisk(responseBody.body(),
                            finalFile);
                    if (saved)
                        return finalFile;
                    else
                        throw new CannotSaveFileToDownloadFolder();
                });

    }

    @Override
    public String getCacheFilename() {
        return "remote_files_local.data";
    }

    public static class FileConfirmationWrapper {
        FileAPI.ConfirmFileResponse response;
        String token;

        public FileConfirmationWrapper(FileAPI.ConfirmFileResponse response, String token) {
            this.response = response;
            this.token = token;
        }

        public FileAPI.ConfirmFileResponse getResponse() {
            return response;
        }

        public void setResponse(FileAPI.ConfirmFileResponse response) {
            this.response = response;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
