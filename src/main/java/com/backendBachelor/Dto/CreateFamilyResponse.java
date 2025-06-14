package com.backendBachelor.Dto;

// Response DTOs
public class CreateFamilyResponse {
    private boolean success;
    private String message;
    private String familyId;
    private FamilyDto family;

    public CreateFamilyResponse() {
    }

    public CreateFamilyResponse(boolean success, String message, String familyId, FamilyDto family) {
        this.success = success;
        this.message = message;
        this.familyId = familyId;
        this.family = family;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public FamilyDto getFamily() {
        return family;
    }

    public void setFamily(FamilyDto family) {
        this.family = family;
    }
}
