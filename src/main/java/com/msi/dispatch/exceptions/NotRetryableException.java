package com.msi.dispatch.exceptions;

public class NotRetryableException extends RuntimeException {

    public NotRetryableException(Exception exception) {
        super(exception);
    }
}
