package com.webserver.exception;

/**
 * @author LeafDust
 * @create 2019-09-01 23:07
 */
public class EmptyRequestException extends Exception{
    public EmptyRequestException() {
        super();
    }

    public EmptyRequestException(String message) {
        super(message);
    }

    public EmptyRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyRequestException(Throwable cause) {
        super(cause);
    }

    protected EmptyRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
