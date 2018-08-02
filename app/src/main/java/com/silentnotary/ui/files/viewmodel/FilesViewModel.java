package com.silentnotary.ui.files.viewmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.silentnotary.ui.files.view.FilesFragment;
import com.silentnotary.api.requestmanager.FileRequestManager;
import com.silentnotary.dao.UploadedFilesConfirmationDao;
import com.silentnotary.dao.model.UploadedFile;
import com.silentnotary.dao.model.UploadedFilesConfirmation;
import com.silentnotary.model.File;
import com.silentnotary.model.PendingFile;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;

/**
 * Created by albert on 9/27/17.
 */

public class FilesViewModel {
    private FilesFragment filesFragment;
    private FileRequestManager fileRequestManager;
    private UploadedFilesConfirmationDao uploadedFilesConfirmationDao
            = new UploadedFilesConfirmationDao();

    public static final String TAG = FilesViewModel.class.getSimpleName();

    public static class RationalMessageNeedToBeShownException extends Exception {
    }


    public FilesViewModel(FilesFragment rxFragment) {
        this.filesFragment = rxFragment;
        fileRequestManager = new FileRequestManager(rxFragment.getActivity());
    }

    public Observable<List<File>> getAllFiles() {
        Observable<List<File>> remoteFilesObservable = fileRequestManager
                .getAllFiles()
                .onErrorResumeNext(
                        err -> {
                            return Observable.empty();
                        })
                .compose(filesFragment.bindToLifecycle())
                .subscribeOn(Schedulers.newThread());

        Observable<List<UploadedFilesConfirmation>> pendingFilesObservable = uploadedFilesConfirmationDao
                .getAll()
                .subscribeOn(Schedulers.newThread());

        return Observable.combineLatest(remoteFilesObservable, pendingFilesObservable, (remote, pending) -> {
            List<File> files = new ArrayList<>();
            Collections.sort(remote, (f1, f2) -> (int) (f2.getCreatedTimestamp() - f1.getCreatedTimestamp()));
            for (UploadedFilesConfirmation confirmation : pending) {
                RealmList<UploadedFile> pendingFiles = confirmation.getFiles();
                for (UploadedFile file : pendingFiles) {
                    files.add(new PendingFile(confirmation.getConfirmationTokenHex(),
                            confirmation.getCreatedAt(), file.getSize(),
                            confirmation.getComment(), file.getFilename()));
                }
            }
            for (File file : remote) {
                if (file.getType().equals("Upload"))
                    files.add(file);
            }
            return files;
        })
                .compose(filesFragment.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<File>> searchFiles(final String query) {
        return getAllFiles()
                .map(it -> {
                    List<File> files = new ArrayList<>();
                    for (File file : it) {
                        if (file.filterQuery(query)) {
                            files.add(file);
                        }
                    }
                    return files;
                });
    }
}
