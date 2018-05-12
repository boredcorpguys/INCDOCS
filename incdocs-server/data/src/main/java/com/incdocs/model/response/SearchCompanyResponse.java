package com.incdocs.model.response;

public class SearchCompanyResponse {
    private String companyName;
    private String companyId;
    private String groupHeadName;
    private String groupHeadId;
    private String pan;

    public String getCompanyName() {
        return companyName;
    }

    public SearchCompanyResponse setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getCompanyId() {
        return companyId;
    }

    public SearchCompanyResponse setCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }

    public String getGroupHeadName() {
        return groupHeadName;
    }

    public SearchCompanyResponse setGroupHeadName(String groupHeadName) {
        this.groupHeadName = groupHeadName;
        return this;
    }

    public String getGroupHeadId() {
        return groupHeadId;
    }

    public SearchCompanyResponse setGroupHeadId(String groupHeadId) {
        this.groupHeadId = groupHeadId;
        return this;
    }

    public String getPan() {
        return pan;
    }

    public SearchCompanyResponse setPan(String pan) {
        this.pan = pan;
        return this;
    }
}
