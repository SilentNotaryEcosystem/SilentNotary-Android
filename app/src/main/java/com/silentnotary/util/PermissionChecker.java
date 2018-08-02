package com.silentnotary.util;

import android.Manifest;
import android.app.Activity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import com.silentnotary.ui.files.viewmodel.FilesViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by albert on 10/16/17.
 */

public class PermissionChecker {
    public static Observable<Object> checkPermission(Activity activity){

        final boolean denied[] = {false};
        return Observable.create(s -> {
            Dexter.withActivity(activity)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO
                    ).withListener(new MultiplePermissionsListener() {
                @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if(denied[0]) return;

                    if(report != null && report.isAnyPermissionPermanentlyDenied()){
                        s.onError(new FilesViewModel.RationalMessageNeedToBeShownException());
                    }else{
                        s.onNext(true);
                        s.onComplete();
                    }
                }
                @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    s.onError(new FilesViewModel.RationalMessageNeedToBeShownException());
                }
            }).check();
        }).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Object> checkStoragePermission(Activity activity){

        final boolean denied[] = {false};
        return Observable.create(s -> {
            Dexter.withActivity(activity)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ).withListener(new MultiplePermissionsListener() {
                @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if(denied[0]) return;

                    if(report != null && report.isAnyPermissionPermanentlyDenied()){
                        s.onError(new FilesViewModel.RationalMessageNeedToBeShownException());
                    }else{
                        s.onNext(true);
                        s.onComplete();
                    }
                }
                @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    s.onError(new FilesViewModel.RationalMessageNeedToBeShownException());
                }
            }).check();
        }).observeOn(AndroidSchedulers.mainThread());
    }
}
