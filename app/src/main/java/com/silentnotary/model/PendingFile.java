package com.silentnotary.model;

/**
 * Created by albert on 10/4/17.
 */

public class PendingFile extends File {

    String filename;

    public PendingFile() {
    }

    public PendingFile(String hash, long createdTimestamp,
                       long size, String comment, String filename) {
        super(hash, createdTimestamp, 0, "", size, comment, "", "");
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
