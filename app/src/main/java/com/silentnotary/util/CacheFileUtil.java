package com.silentnotary.util;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by albert on 10/3/17.
 */

public class CacheFileUtil {

    private static HashMap<String, File> cache = new HashMap<>();

    public static File createRandomFileByKey(String key, String extension, Context context) throws IOException {
        File file = FileUtil.generateRandomFileInCacheWithExtension(context, extension);
        cache.put(key, file);
        return file;
    }

    public static File getFileByKey(String key){
        return cache.get(key);
    }
}
