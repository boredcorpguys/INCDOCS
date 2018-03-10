package com.incdocs.utils;

import org.springframework.http.HttpStatus;

public class ApplicationException extends Exception {
    private final HttpStatus httpStatusCode;

    public ApplicationException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        httpStatusCode = httpStatus;
    }

    public ApplicationException(String message, HttpStatus httpStatus) {
        super(message);
        httpStatusCode = httpStatus;
    }
}
