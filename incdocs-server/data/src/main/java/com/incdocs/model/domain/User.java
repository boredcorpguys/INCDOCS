package com.incdocs.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.incdocs.model.constants.ApplicationConstants;

public class User {
    private final String incdocsID;
    private String empID;
    private String managerID;
    private String emailID;
    private String name;
    private Integer roleID;
    private String password;
    private String companyID;
    private String contactNumber;
    private boolean isClient;
    private ApplicationConstants.UserStatus status;

    public User(String incdocsID) {
        this.incdocsID = incdocsID;
    }

    public String getIncdocsID() {
        return incdocsID;
    }

    public String getEmpID() {
        return empID;
    }

    public User setEmpID(String empID) {
        this.empID = empID;
        return this;
    }

    public String getManagerID() {
        return managerID;
    }

    public User setManagerID(String managerID) {
        this.managerID = managerID;
        return this;
    }

    public String getEmailID() {
        return emailID;
    }

    public User setEmailID(String emailID) {
        this.emailID = emailID;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public User setRoleID(Integer roleID) {
        this.roleID = roleID;
        return this;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getCompanyID() {
        return companyID;
    }

    public User setCompanyID(String companyID) {
        this.companyID = companyID;
        return this;
    }

    @JsonIgnore
    public String getContactNumber() {
        return contactNumber;
    }

    public User setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getIncdocsID().equals(user.getIncdocsID());
    }

    @Override
    public int hashCode() {
        return getIncdocsID().hashCode();
    }

    public boolean isClient() {
        return isClient;
    }

    public User setClient(boolean client) {
        isClient = client;
        return this;
    }

    public ApplicationConstants.UserStatus getStatus() {
        return status;
    }

    public User setStatus(ApplicationConstants.UserStatus status) {
        this.status = status;
        return this;
    }
}
