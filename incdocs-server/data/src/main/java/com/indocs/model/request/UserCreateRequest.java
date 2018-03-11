package com.indocs.model.request;

public class UserCreateRequest {
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

    public UserCreateRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getEntityID() {
        return entityID;
    }

    public UserCreateRequest setEntityID(String entityID) {
        this.entityID = entityID;
        return this;
    }

    public String getGhID() {
        return ghID;
    }

    public UserCreateRequest setGhID(String ghID) {
        this.ghID = ghID;
        return this;
    }

    public int getRoleID() {
        return roleID;
    }

    public UserCreateRequest setRoleID(int roleID) {
        this.roleID = roleID;
        return this;
    }

    public String getCompanyID() {
        return companyID;
    }

    public UserCreateRequest setCompanyID(String companyID) {
        this.companyID = companyID;
        return this;
    }

    public boolean isClient() {
        return isClient;
    }

    public UserCreateRequest setClient(boolean client) {
        isClient = client;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserCreateRequest setName(String name) {
        this.name = name;
        return this;
    }
}
