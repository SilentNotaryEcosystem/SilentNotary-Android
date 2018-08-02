package com.silentnotary.ui.files.viewmodel;

import java.io.File;
import java.util.List;

import com.silentnotary.ui.files.view.UploadFilesFragment;
import com.silentnotary.api.exception.CannotUploadFilesException;
import com.silentnotary.api.requestmanager.FileRequestManager;
import com.silentnotary.api.retrofit.FileAPI;
import com.silentnotary.dao.UploadedFilesConfirmationDao;
import com.silentnotary.util.PermissionChecker;
import com.silentnotary.widget.FilePickerBuilder;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by albert on 10/3/17.
 */

public class UploadFileViewModel {
    private final UploadFilesFragment filesFragment;
    private FileRequestManager fileRequestManager;

    public UploadFileViewModel(UploadFilesFragment rxFragment) {
        this.filesFragment = rxFragment;
        fileRequestManager = new FileRequestManager(rxFragment.getActivity());
    }

    public Observable<FileAPI.UploadFileResponse> upload(List<File> files, String comment) {
        if (files.isEmpty()) return Observable.empty();

        return
                fileRequestManager
                        .uploadFiles(files, comment, filesFragment.getContext())
                        .compose(filesFragment.bindToLifecycle())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(resp -> {
                            if (!resp.isUploadSuccesefully()) {
                                throw new CannotUploadFilesException();
                            }
                            return resp;
                        });
    }

    public Observable<File> getFileFromStorage() {
        return PermissionChecker.checkPermission(filesFragment.getActivity())
                .flatMap(e -> FilePickerBuilder.build(filesFragment.getContext()));
    }

    public Observable<String> saveTransactionToDb(String confirmationToken,
                                                  String comment,
                                                  List<File> files
    ) {
        UploadedFilesConfirmationDao uploadedFilesConfirmationDao = new UploadedFilesConfirmationDao();


        return uploadedFilesConfirmationDao.create(confirmationToken, comment, System.currentTimeMillis(),
                files)
                .compose(filesFragment.bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public enum UploadType {
        FILE,
        VIDEO,
        PHOTO
    }

}
