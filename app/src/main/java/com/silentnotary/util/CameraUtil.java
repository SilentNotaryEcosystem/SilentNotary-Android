package com.silentnotary.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.trello.rxlifecycle2.LifecycleProvider;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by albert on 9/28/17.
 */

public class CameraUtil {

    static File cameraFile = null;

    public static Intent getCapturePhotoIntent(Context context) {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            cameraFile
                    = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));


            return takePictureIntent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent getCaptureVideoIntent(Context context) throws IOException {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        if (takeVideoIntent.resolveActivity(context.getPackageManager()) != null) {
            return takeVideoIntent;
        }
        return null;
    }

    public static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public static Observable<File> copyFromMediaStoreIntoCache(Uri uri, Context context,
                                                               LifecycleProvider lifecycleProvider,
                                                               String extension) throws IOException {
        return Observable.<File>create(s -> {

            File srcFile = null;
            if (uri != null) {
                srcFile = new File(getRealPathFromURI(uri, context));
            } else {
                srcFile = cameraFile;
            }

            File randomFileByKey = CacheFileUtil.createRandomFileByKey("file", extension, context);
            FileUtil.copy(srcFile, randomFileByKey);
            srcFile.delete();
            s.onNext(randomFileByKey);
            s.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycleProvider.bindToLifecycle());

    }
}
