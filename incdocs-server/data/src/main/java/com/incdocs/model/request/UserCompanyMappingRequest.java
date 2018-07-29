package com.incdocs.model.request;

public class UserCompanyMappingRequest {
    private String memberId;
    private String companyId;

    public String getMemberId() {
        return memberId;
    }

    public UserCompanyMappingRequest setMemberId(String memberId) {
        this.memberId = memberId;
        return this;
    }

    public String getCompanyId() {
        return companyId;
    }

    public UserCompanyMappingRequest setCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }
}
