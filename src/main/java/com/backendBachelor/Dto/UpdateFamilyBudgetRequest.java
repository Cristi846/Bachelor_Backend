package com.backendBachelor.Dto;

import com.backendBachelor.Entity.Family;

public class UpdateFamilyBudgetRequest {
    private String userId;
    private Family.FamilyBudget sharedBudget;

    public UpdateFamilyBudgetRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Family.FamilyBudget getSharedBudget() {
        return sharedBudget;
    }

    public void setSharedBudget(Family.FamilyBudget sharedBudget) {
        this.sharedBudget = sharedBudget;
    }
}
