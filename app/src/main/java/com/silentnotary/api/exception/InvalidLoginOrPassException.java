package com.silentnotary.api.exception;

/**
 * Created by albert on 9/29/17.
 */

public class InvalidLoginOrPassException extends Exception {
    public InvalidLoginOrPassException() {
        super("Invalid login or password");
    }
}
