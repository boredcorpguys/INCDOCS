package com.incdocs.model.domain;

import com.incdocs.model.constants.ApplicationConstants;

public class BulkUploadUserRow implements Comparable<BulkUploadUserRow> {
    private String empID;
    private String role;
    private String ghID;
    private String name;
    private String errorStatus;

    public String getName() {
        return name;
    }

    public BulkUploadUserRow setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmpID() {
        return empID;
    }

    public BulkUploadUserRow setEmpID(String empID) {
        this.empID = empID;
        return this;
    }

    public String getRole() {
        return role;
    }

    public BulkUploadUserRow setRole(String role) {
        this.role = role;
        return this;
    }

    public String getGhID() {
        return ghID;
    }

    public BulkUploadUserRow setGhID(String ghID) {
        this.ghID = ghID;
        return this;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    public BulkUploadUserRow setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
        return this;
    }

    @Override
    public int compareTo(BulkUploadUserRow o) {
        Integer thisRolePriority = ApplicationConstants.Roles.valueOf(this.getRole()).getPriority();
        Integer thatRolePriority = ApplicationConstants.Roles.valueOf(o.getRole()).getPriority();
        return thisRolePriority.compareTo(thatRolePriority);
    }
}
