package com.incdocs.model.response;

public class SearchSubordinateResponse {
    private String empID;
    private String name;

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
}
