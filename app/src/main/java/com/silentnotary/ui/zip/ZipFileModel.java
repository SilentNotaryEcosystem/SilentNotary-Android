package com.silentnotary.ui.zip;

import java.io.File;

import com.silentnotary.util.FileUtil;

/**
 * Created by albert on 10/27/17.
 */

public class ZipFileModel {
    private File file;

    public ZipFileModel(File file) {

        this.file = file;
    }

    public String getName() {
        return file.getName();
    }

    public String getSizeFormated() {
        return FileUtil.readableFileSize(file.length());
    }

    public File getFile() {
        return file;
    }
}
