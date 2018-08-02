package com.silentnotary.model;

import android.net.Uri;

/**
 * Created by albert on 10/3/17.
 */
public  class FileToUpload{
    public FileToUpload(String comment, Uri fileUri) {
        this.comment = comment;
        this.fileUri = fileUri;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    private String comment;
    private Uri fileUri;
}
