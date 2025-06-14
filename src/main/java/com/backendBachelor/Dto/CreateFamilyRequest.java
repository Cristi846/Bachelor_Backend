package com.backendBachelor.Dto;

// Request DTOs
public class CreateFamilyRequest {
    private String familyName;
    private String userId;

    public CreateFamilyRequest() {
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
