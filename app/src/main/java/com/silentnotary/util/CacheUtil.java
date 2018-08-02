package com.silentnotary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by albert on 10/12/17.
 */

public class CacheUtil {

    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
    private int quality = 100;

    public CacheUtil() {

    }

    public CacheUtil(Bitmap.CompressFormat compressFormat, int quality) {
        this.compressFormat = compressFormat;
        this.quality = quality;
    }

    public static String convertKey(String key) {
        return key.replaceAll("[^A-Za-z0-9]", "");
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                in.close();
            } catch (Exception e) {

            }
        }
    }

    public void saveToCacheAsFile(InputStream inputStream, Context ctx, String key) throws IOException {
        key = convertKey(key);
        File fileFromCacheStorage = FileUtil.getFileFromCacheStorage(ctx, key);
        if (!fileFromCacheStorage.exists()) fileFromCacheStorage.createNewFile();
        copyInputStreamToFile(inputStream, fileFromCacheStorage);
    }


    public
    @Nullable
    File readFileFromCache(Context ctx, String key) {
        return FileUtil.getFileFromCacheStorage(ctx, convertKey(key));
    }

    public
    @Nullable
    InputStream readFromCache(Context cxt, String key) throws FileNotFoundException {
        File fileFromCacheStorage = FileUtil.getFileFromCacheStorage(cxt, convertKey(key));
        if (!fileFromCacheStorage.exists()) return null;
        return new FileInputStream(fileFromCacheStorage);
    }
}

