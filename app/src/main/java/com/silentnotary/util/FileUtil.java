package com.silentnotary.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Random;

import okhttp3.ResponseBody;

/**
 * Created by albert on 9/28/17.
 */

public class FileUtil {

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String toCorrectFileName(String myString) {
        return myString.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    public static String getMimeType(Uri uri) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri.getPath());
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getRandomFname() {
        String f = "";
        for (int i = 0; i < 7; i++) {
            f += new Random().nextInt(10);
        }
        return f;
    }

    public static File getFileFromAppStorage(Context ctx, String filename) throws IOException {
        File file = new File(ctx.getFilesDir(), filename);
        if (!file.exists()) file.createNewFile();
        return file;
    }


    public static File getFileFromAppStorageNoCreate(Context ctx, String filename) {
        return new File(ctx.getFilesDir(), filename);
    }

    public static File getFileFromCacheStorage(Context ctx, String filename) {
        return new File(ctx.getCacheDir(), filename);
    }

    public static File generateRandomFile(Context context) throws IOException {
        String randomFname = getRandomFname();
        return getFileFromAppStorage(context, randomFname);
    }

    public static File generateRandomFileInCache(Context context) throws IOException {

        File file = new File(context.getCacheDir(), getRandomFname());
        if (!file.exists()) file.createNewFile();
        return file;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dst);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static File generateRandomFileInCacheWithExtension(Context context, String extension) throws IOException {

        File file = new File(context.getCacheDir(), getRandomFname() + "." + extension);
        if (!file.exists()) file.createNewFile();
        return file;
    }

    public static boolean deleteFile(String uri) {
        File file = new File(uri);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static File getFileFromDownloadFolder(String filename, String folder) {

        File downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File dir = new File(downloadDirectory.getPath()
                + File.separator + "SilentNotary" + File.separator + folder);
        dir.mkdirs();
        File file = new File(dir.getPath() + File.separator + filename);

        return file;
    }

    public static boolean writeResponseBodyToDisk(ResponseBody body, File file) {
        try {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }

                outputStream.flush();
                return true;
            } catch (Exception e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}
