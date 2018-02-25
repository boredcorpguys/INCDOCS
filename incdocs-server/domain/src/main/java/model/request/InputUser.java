package model.request;

public class InputUser {
    private String id;
    private String contactNumber;
    private String emailID;
    private String password;

    public String getId() {
        return id;
    }

    public InputUser setId(String id) {
        this.id = id;
        return this;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public InputUser setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    public String getEmailID() {
        return emailID;
    }

    public InputUser setEmailID(String emailID) {
        this.emailID = emailID;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public InputUser setPassword(String password) {
        this.password = password;
        return this;
    }
}
