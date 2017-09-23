package com.agoda.exception;

/**
 * Created by Gaurav on 18/09/17.
 */
public class ApiBlockedException extends Exception {
    /**
     * @param message
     */
    public ApiBlockedException(String message) {
        super(message);
    }
}
