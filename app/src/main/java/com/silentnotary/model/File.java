package com.silentnotary.model;

import java.io.Serializable;

import com.silentnotary.util.FileUtil;
import com.silentnotary.util.TimeUtil;

/**
 * Created by albert on 9/27/17.
 */

public class File implements Serializable {

    String hash;
    long createdTimestamp;
    long expiredTimestamp;
    String type;
    long size;
    String comment;
    String etherTx;
    String none;

    public boolean filterQuery(String query) {
        return hash.contains(query)
                || comment.contains(query);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File)) return false;
        File file = (File) o;
        if (createdTimestamp != file.createdTimestamp) return false;
        if (expiredTimestamp != file.expiredTimestamp) return false;
        if (Float.compare(file.size, size) != 0) return false;
        if (hash != null ? !hash.equals(file.hash) : file.hash != null) return false;
        if (type != null ? !type.equals(file.type) : file.type != null) return false;
        if (comment != null ? !comment.equals(file.comment) : file.comment != null) return false;
        if (etherTx != null ? !etherTx.equals(file.etherTx) : file.etherTx != null) return false;
        return none != null ? none.equals(file.none) : file.none == null;
    }

    @Override
    public int hashCode() {
        int result = hash != null ? hash.hashCode() : 0;
        result = 31 * result + (int) (createdTimestamp ^ (createdTimestamp >>> 32));
        result = 31 * result + (int) (expiredTimestamp ^ (expiredTimestamp >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (size != +0.0f ? Float.floatToIntBits(size) : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (etherTx != null ? etherTx.hashCode() : 0);
        result = 31 * result + (none != null ? none.hashCode() : 0);
        return result;
    }

    public File() {
    }

    public File(String hash, long createdTimestamp, long expiredTimestamp, String type, long size, String comment, String etherTx, String none) {
        this.hash = hash;
        this.createdTimestamp = createdTimestamp;
        this.expiredTimestamp = expiredTimestamp;
        this.type = type;
        this.size = size;
        this.comment = comment;
        this.etherTx = etherTx;
        this.none = none;
    }


    public String getCreatedDateFormated() {
        return TimeUtil.formatTimestampWithSeconds(getCreatedTimestamp());
    }

    public String getExpiredDateFormated() {
        return TimeUtil.formatTimestampWithSeconds(getExpiredTimestamp());
    }

    public String getSizeFormated() {
        return FileUtil.readableFileSize(this.getSize());
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getExpiredTimestamp() {
        return expiredTimestamp;
    }

    public void setExpiredTimestamp(long expiredTimestamp) {
        this.expiredTimestamp = expiredTimestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEtherTx() {
        return etherTx;
    }

    public void setEtherTx(String etherTx) {
        this.etherTx = etherTx;
    }

    public String getNone() {
        return none;
    }

    public void setNone(String none) {
        this.none = none;
    }
}
