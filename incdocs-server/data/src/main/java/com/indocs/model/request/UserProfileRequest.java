package com.indocs.model.request;

public class UserProfileRequest {
    private String id;
    private String contactNumber;
    private String emailID;
    private String password;

    public String getId() {
        return id;
    }

    public UserProfileRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public UserProfileRequest setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    public String getEmailID() {
        return emailID;
    }

    public UserProfileRequest setEmailID(String emailID) {
        this.emailID = emailID;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserProfileRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
