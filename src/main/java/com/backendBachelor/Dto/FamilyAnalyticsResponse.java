package com.backendBachelor.Dto;

import java.util.List;
import java.util.Map;

public class FamilyAnalyticsResponse {
    private Map<String, Double> categorySpending;
    private Map<String, Double> memberSpending;
    private double totalFamilySpending;
    private double remainingBudget;
    private List<String> overBudgetCategories;

    public FamilyAnalyticsResponse() {
    }

    // Getters and Setters
    public Map<String, Double> getCategorySpending() {
        return categorySpending;
    }

    public void setCategorySpending(Map<String, Double> categorySpending) {
        this.categorySpending = categorySpending;
    }

    public Map<String, Double> getMemberSpending() {
        return memberSpending;
    }

    public void setMemberSpending(Map<String, Double> memberSpending) {
        this.memberSpending = memberSpending;
    }

    public double getTotalFamilySpending() {
        return totalFamilySpending;
    }

    public void setTotalFamilySpending(double totalFamilySpending) {
        this.totalFamilySpending = totalFamilySpending;
    }

    public double getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(double remainingBudget) {
        this.remainingBudget = remainingBudget;
    }

    public List<String> getOverBudgetCategories() {
        return overBudgetCategories;
    }

    public void setOverBudgetCategories(List<String> overBudgetCategories) {
        this.overBudgetCategories = overBudgetCategories;
    }
}
