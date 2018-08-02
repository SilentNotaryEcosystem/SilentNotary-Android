package com.silentnotary.api.requestmanager;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.silentnotary.util.FileUtil;

import io.reactivex.ObservableEmitter;


/**
 * Created by albert on 10/9/17.
 */

public abstract class CachableRequestManager<T> {

    private Context context;

    public CachableRequestManager(Context context) {
        this.context = context;
    }

    public abstract String getCacheFilename();

    public Object getObjectFromCache() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(FileUtil.getFileFromAppStorage(context, getCacheFilename()));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object object = objectInputStream.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void saveObjectToCache(Object object) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(FileUtil.getFileFromAppStorage(context, getCacheFilename()));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void tryToReadFromCache(ObservableEmitter<T> emitter, T defaultValue) {
        try {
            Object objectFromCache = getObjectFromCache();
            if (objectFromCache != null)
                emitter.onNext((T) getObjectFromCache());
            else
                emitter.onNext(defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
