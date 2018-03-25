package com.incdocs.model.response;

import java.util.Date;

public class ErrorResponse {
    private final Date timestamp;
    private final String message;

    public ErrorResponse(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
