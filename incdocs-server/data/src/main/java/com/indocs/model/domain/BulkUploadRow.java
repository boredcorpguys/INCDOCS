package com.indocs.model.domain;

public class BulkUploadRow {
    String empID;
    String role;
    String ghID;
    String companyID;

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

    public String getCompanyID() {
        return companyID;
    }

    public BulkUploadRow setCompanyID(String companyID) {
        this.companyID = companyID;
        return this;
    }
}
