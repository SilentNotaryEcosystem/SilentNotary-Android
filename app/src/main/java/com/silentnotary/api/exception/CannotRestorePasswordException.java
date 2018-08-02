package com.silentnotary.api.exception;

/**
 * Created by albert on 10/10/17.
 */

public class CannotRestorePasswordException extends Exception {
    public CannotRestorePasswordException(){
        super("Cannot restore password");
    }
}
