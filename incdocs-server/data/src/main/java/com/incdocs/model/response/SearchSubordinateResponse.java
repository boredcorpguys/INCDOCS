package com.incdocs.model.response;

public class SearchSubordinateResponse {
    private String incdocsID;
    private String empID;
    private String name;
    private Integer roleID;
    private String companyID;
    private String managerID;

    public String getIncdocsID() {
        return incdocsID;
    }

    public SearchSubordinateResponse setIncdocsID(String incdocsID) {
        this.incdocsID = incdocsID;
        return this;
    }

    public String getEmpID() {
        return empID;
    }

    public SearchSubordinateResponse setEmpID(String empID) {
        this.empID = empID;
        return this;
    }

    public String getName() {
        return name;
    }

    public SearchSubordinateResponse setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public SearchSubordinateResponse setRoleID(Integer roleID) {
        this.roleID = roleID;
        return this;
    }

    public String getCompanyID() {
        return companyID;
    }

    public SearchSubordinateResponse setCompanyID(String companyID) {
        this.companyID = companyID;
        return this;
    }

    public String getManagerID() {
        return managerID;
    }

    public SearchSubordinateResponse setManagerID(String managerID) {
        this.managerID = managerID;
        return this;
    }
}
