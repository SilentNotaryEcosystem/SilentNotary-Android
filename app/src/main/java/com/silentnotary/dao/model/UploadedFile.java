package com.silentnotary.dao.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by albert on 10/4/17.
 */

public class UploadedFile extends RealmObject {

    @PrimaryKey
    private long id;
    String filename;
    Long createdAt;
    Long size;

    public UploadedFile() {
    }

    public UploadedFile(long id, String filename, Long createdAt, Long size) {
        this.id = id;
        this.filename = filename;
        this.createdAt = createdAt;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UploadedFile)) return false;
        UploadedFile that = (UploadedFile) o;
        if (id != that.id) return false;
        if (filename != null ? !filename.equals(that.filename) : that.filename != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null)
            return false;
        return size != null ? size.equals(that.size) : that.size == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }


}

