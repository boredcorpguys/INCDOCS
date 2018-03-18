package com.incdocs.model.domain;

import com.incdocs.model.constants.ApplicationConstants;

public class BulkUploadRow implements Comparable<BulkUploadRow> {
    private String empID;
    private String role;
    private String ghID;
    private String name;
    private String errorStatus;

    public String getName() {
        return name;
    }

    public BulkUploadRow setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmpID() {
        return empID;
    }

    public BulkUploadRow setEmpID(String empID) {
        this.empID = empID;
        return this;
    }

    public String getRole() {
        return role;
    }

    public BulkUploadRow setRole(String role) {
        this.role = role;
        return this;
    }

    public String getGhID() {
        return ghID;
    }

    public BulkUploadRow setGhID(String ghID) {
        this.ghID = ghID;
        return this;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    public BulkUploadRow setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
        return this;
    }

    @Override
    public int compareTo(BulkUploadRow o) {
        Integer thisRolePriority = ApplicationConstants.Roles.valueOf(this.getRole()).getPriority();
        Integer thatRolePriority = ApplicationConstants.Roles.valueOf(o.getRole()).getPriority();
        return thisRolePriority.compareTo(thatRolePriority);
    }
}
