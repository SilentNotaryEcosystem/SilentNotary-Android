package com.silentnotary.api.exception;

/**
 * Created by albert on 10/9/17.
 */

public class CannotDeleteFileException extends Exception {
    public CannotDeleteFileException(String message){
        super(message);
    }
}
