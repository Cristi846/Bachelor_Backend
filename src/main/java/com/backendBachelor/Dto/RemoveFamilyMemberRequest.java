package com.backendBachelor.Dto;

public class RemoveFamilyMemberRequest {
    private String adminUserId;
    private String memberUserId;

    public RemoveFamilyMemberRequest() {
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getMemberUserId() {
        return memberUserId;
    }

    public void setMemberUserId(String memberUserId) {
        this.memberUserId = memberUserId;
    }
}
