package com.indocs.model.request;

public class CreateUserRequest {
    private String name;
    private String id;
    private String entityID;
    private int roleID;
    private String ghID;
    private String companyID;
    private boolean isClient;

    public String getId() {
        return id;
    }

    public CreateUserRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getEntityID() {
        return entityID;
    }

    public CreateUserRequest setEntityID(String entityID) {
        this.entityID = entityID;
        return this;
    }

    public String getGhID() {
        return ghID;
    }

    public CreateUserRequest setGhID(String ghID) {
        this.ghID = ghID;
        return this;
    }

    public int getRoleID() {
        return roleID;
    }

    public CreateUserRequest setRoleID(int roleID) {
        this.roleID = roleID;
        return this;
    }

    public String getCompanyID() {
        return companyID;
    }

    public CreateUserRequest setCompanyID(String companyID) {
        this.companyID = companyID;
        return this;
    }

    public boolean isClient() {
        return isClient;
    }

    public CreateUserRequest setClient(boolean client) {
        isClient = client;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateUserRequest setName(String name) {
        this.name = name;
        return this;
    }
}
