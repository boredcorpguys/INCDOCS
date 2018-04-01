package com.incdocs.model.request;

public class CreateCompanyRequest {
    private String id;
    private String name;
    private String pan;
    private String ghID;
    private boolean isClient;

    public boolean isClient() {
        return isClient;
    }

    public CreateCompanyRequest setClient(boolean client) {
        isClient = client;
        return this;
    }

    public String getId() {
        return id;
    }

    public CreateCompanyRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateCompanyRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getPan() {
        return pan;
    }

    public CreateCompanyRequest setPan(String pan) {
        this.pan = pan;
        return this;
    }

    public String getGhID() {
        return ghID;
    }

    public CreateCompanyRequest setGhID(String ghID) {
        this.ghID = ghID;
        return this;
    }
}
