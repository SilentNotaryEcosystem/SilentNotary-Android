package com.silentnotary.ui.files.viewmodel;

import com.trello.rxlifecycle2.components.support.RxFragment;

import java.io.File;

import com.silentnotary.api.exception.CannotDeleteFileException;
import com.silentnotary.api.requestmanager.FileRequestManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by albert on 10/12/17.
 */

public class DetailFileViewModel {
    private RxFragment fragment;
    private FileRequestManager fileRequestManager = null;

    public DetailFileViewModel(RxFragment fragment){
        this.fragment = fragment;
        fileRequestManager =  new FileRequestManager(fragment.getActivity());
    }


    public Observable<File> downloadFile(com.silentnotary.model.File file) {
        return fileRequestManager
                .downloadFile(file.getHash(), file.getNone(),"files")
                .compose(fragment.bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<com.silentnotary.model.File> deleteFile(com.silentnotary.model.File file) {
        return fileRequestManager.deleteFile(file.getHash(), file.getNone())
                .map(response -> {
                    if(!response.isSuccess()){
                        throw new CannotDeleteFileException(response.getStatus());
                    }
                    return file;
                })
                .compose(fragment.bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
