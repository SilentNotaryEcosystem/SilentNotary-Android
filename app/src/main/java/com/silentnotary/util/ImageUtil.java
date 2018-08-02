package com.silentnotary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by albert on 9/28/17.
 */

public class ImageUtil {
    public static boolean isImage(File file){

        String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};
        for (String extension : okFileExtensions)
        {
            if (file.getName().toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }

    public static File saveImageFromBitmap(Context ctx, String filename, Bitmap bitmap) throws IOException {
        File fileFromAppStorage = FileUtil.getFileFromAppStorage(ctx, filename);
        FileOutputStream fileOutPutStream = new FileOutputStream(fileFromAppStorage);
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutPutStream);

        fileOutPutStream.flush();
        fileOutPutStream.close();
        return fileFromAppStorage;
    }

    public static File saveImageToRandomFile(Context ctx, Bitmap bitmap) throws IOException {
        File file = FileUtil.generateRandomFile(ctx);
        FileOutputStream fileOutPutStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, fileOutPutStream);

        fileOutPutStream.flush();
        fileOutPutStream.close();
        return file;
    }

    private static final String TEMP_IMAGE_NAME = "temp_1photo";
    public static File saveTempImage(Context ctx, Bitmap bitmap) throws IOException {
        return ImageUtil.saveImageFromBitmap(ctx, TEMP_IMAGE_NAME, bitmap);
    }

    private static File tempImage;
    public static File createTempImageFile(Context context) throws IOException {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File tempFile = File.createTempFile(TEMP_IMAGE_NAME,
                ".jpg",
                storageDir);
        tempImage = tempFile;
        return tempFile;
    }
    public static File getTempImageFile(Context context){
        return tempImage;
    }
    public static Bitmap getTempImageBitmap(Context context){
        return BitmapFactory.decodeFile(TEMP_IMAGE_NAME);
    }
    public static Uri getTempImageUri(Context context) throws IOException {
        return Uri.fromFile(FileUtil.getFileFromAppStorage(context, TEMP_IMAGE_NAME));
    }

}
