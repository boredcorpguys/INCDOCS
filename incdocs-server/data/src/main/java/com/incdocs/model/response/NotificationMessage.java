package com.incdocs.model.response;

public class NotificationMessage {
    private String successMessage;
    private String errorMessage;

    public String getSuccessMessage() {
        return successMessage;
    }

    public NotificationMessage setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public NotificationMessage setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
