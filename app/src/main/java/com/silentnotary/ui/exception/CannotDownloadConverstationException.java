package com.silentnotary.ui.exception;

/**
 * Created by albert on 10/26/17.
 */

public class CannotDownloadConverstationException extends Exception {

    public CannotDownloadConverstationException() {
        super("Cannot load conversation, it seems data is not loaded yet.");
    }
}
