package com.silentnotary.dao.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by albert on 10/4/17.
 */

public class UploadedFilesConfirmation extends RealmObject{
    @PrimaryKey
    private String ConfirmationTokenHex;
    private Long createdAt;
    private RealmList<UploadedFile> files = new RealmList<>();
    String comment;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UploadedFilesConfirmation)) return false;

        UploadedFilesConfirmation that = (UploadedFilesConfirmation) o;

        if (ConfirmationTokenHex != null ? !ConfirmationTokenHex.equals(that.ConfirmationTokenHex) : that.ConfirmationTokenHex != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null)
            return false;
        if (files != null ? !files.equals(that.files) : that.files != null) return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;

    }

    @Override
    public int hashCode() {
        int result = ConfirmationTokenHex != null ? ConfirmationTokenHex.hashCode() : 0;
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (files != null ? files.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RealmList<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(RealmList<UploadedFile> files) {
        this.files = files;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getConfirmationTokenHex() {
        return ConfirmationTokenHex;
    }

    public void setConfirmationTokenHex(String confirmationTokenHex) {
        ConfirmationTokenHex = confirmationTokenHex;
    }
}
