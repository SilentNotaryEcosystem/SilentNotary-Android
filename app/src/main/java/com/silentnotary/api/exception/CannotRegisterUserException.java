package com.silentnotary.api.exception;

/**
 * Created by albert on 9/29/17.
 */

public class CannotRegisterUserException extends Exception{
    public CannotRegisterUserException() {
        super("Cannot register, this username already exist!");
    }
}
