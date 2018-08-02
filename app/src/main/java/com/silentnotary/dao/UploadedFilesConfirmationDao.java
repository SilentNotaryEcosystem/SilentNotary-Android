package com.silentnotary.dao;

import java.io.File;
import java.util.List;
import java.util.Random;

import com.silentnotary.dao.model.UploadedFile;
import com.silentnotary.dao.model.UploadedFilesConfirmation;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by albert on 10/4/17.
 */

public class UploadedFilesConfirmationDao {
    public Observable<String> create(String token,
                                     String comment,
                                     Long createdAt,
                                     List<File> files){

        return Observable.<String>create(s -> {
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                UploadedFilesConfirmation uploadedFilesConfirmation = realm.createObject(UploadedFilesConfirmation.class, token);
                uploadedFilesConfirmation.setCreatedAt(createdAt);
                uploadedFilesConfirmation.setComment(comment);
                for (File file : files) {
                    UploadedFile uploadedFile = realm.createObject(UploadedFile.class, new Random().nextInt());
                    uploadedFile.setCreatedAt(createdAt);
                    uploadedFile.setFilename(file.getName());
                    uploadedFile.setSize(file.length());
                    uploadedFilesConfirmation.getFiles()
                            .add(uploadedFile);
                }
                realm.commitTransaction();
                s.onNext(token);
                s.onComplete();
            } catch (Exception e) {
                s.onError(e);
            } finally {
                if (realm != null)
                    realm.close();
            }
        });
    }

    public Observable<String> removeConfirmation(String confirmationToken) {
        return Observable.create(s -> {
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();

                RealmResults<UploadedFilesConfirmation> confirmations = realm.where(UploadedFilesConfirmation.class)
                        .equalTo("ConfirmationTokenHex", confirmationToken)
                        .findAll();
                if (confirmations.size() == 0) {
                    s.onComplete();
                } else {
                    realm.beginTransaction();
                    boolean deleted = confirmations.deleteAllFromRealm();
                    realm.commitTransaction();
                    if (deleted) {
                        s.onNext(confirmationToken);
                    }
                    s.onComplete();

                }
            } catch (Exception e) {
                s.onError(e);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    public Observable<List<UploadedFilesConfirmation>> getAll() {
        return Observable.create(s -> {
            Realm realm = null;
            try {
                realm = Realm.getDefaultInstance();
                s.onNext(
                        realm.copyFromRealm(realm.where(UploadedFilesConfirmation.class)
                                .findAll())
                );

                s.onComplete();
            } catch (Exception e) {
                s.onError(e);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }
}
