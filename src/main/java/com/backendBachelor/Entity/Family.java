package com.backendBachelor.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Family {
    private String id;
    private String name;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<String> members;
    private List<String> adminUsers;
    private FamilyBudget sharedBudget;
    private FamilySettings settings;

    // Default constructor for Firebase
    public Family() {
        this.members = new ArrayList<>();
        this.adminUsers = new ArrayList<>();
        this.sharedBudget = new FamilyBudget();
        this.settings = new FamilySettings();
        this.createdAt = LocalDateTime.now();
    }

    // Constructor with parameters
    public Family(String name, String createdBy) {
        this();
        this.name = name;
        this.createdBy = createdBy;
        this.adminUsers.add(createdBy);
        this.members.add(createdBy);
    }

    // Inner class for family budget
    public static class FamilyBudget {
        private double monthlyBudget;
        private String currency;
        private Map<String, Double> categoryBudgets;

        public FamilyBudget() {
            this.monthlyBudget = 0.0;
            this.currency = "USD";
            this.categoryBudgets = new HashMap<>();
            initDefaultCategoryBudgets();
        }

        private void initDefaultCategoryBudgets() {
            categoryBudgets.put("Food", 0.0);
            categoryBudgets.put("Transportation", 0.0);
            categoryBudgets.put("Housing", 0.0);
            categoryBudgets.put("Entertainment", 0.0);
            categoryBudgets.put("Utilities", 0.0);
            categoryBudgets.put("Healthcare", 0.0);
            categoryBudgets.put("Shopping", 0.0);
            categoryBudgets.put("Other", 0.0);
        }

        // Getters and Setters
        public double getMonthlyBudget() { return monthlyBudget; }
        public void setMonthlyBudget(double monthlyBudget) { this.monthlyBudget = monthlyBudget; }

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }

        public Map<String, Double> getCategoryBudgets() { return categoryBudgets; }
        public void setCategoryBudgets(Map<String, Double> categoryBudgets) { this.categoryBudgets = categoryBudgets; }
    }

    // Inner class for family settings
    public static class FamilySettings {
        private boolean allowMembersToAddExpenses;
        private boolean requireApprovalForExpenses;
        private double expenseApprovalThreshold;
        private boolean allowPersonalExpenses;

        public FamilySettings() {
            this.allowMembersToAddExpenses = true;
            this.requireApprovalForExpenses = false;
            this.expenseApprovalThreshold = 100.0;
            this.allowPersonalExpenses = true;
        }

        // Getters and Setters
        public boolean isAllowMembersToAddExpenses() { return allowMembersToAddExpenses; }
        public void setAllowMembersToAddExpenses(boolean allowMembersToAddExpenses) { this.allowMembersToAddExpenses = allowMembersToAddExpenses; }

        public boolean isRequireApprovalForExpenses() { return requireApprovalForExpenses; }
        public void setRequireApprovalForExpenses(boolean requireApprovalForExpenses) { this.requireApprovalForExpenses = requireApprovalForExpenses; }

        public double getExpenseApprovalThreshold() { return expenseApprovalThreshold; }
        public void setExpenseApprovalThreshold(double expenseApprovalThreshold) { this.expenseApprovalThreshold = expenseApprovalThreshold; }

        public boolean isAllowPersonalExpenses() { return allowPersonalExpenses; }
        public void setAllowPersonalExpenses(boolean allowPersonalExpenses) { this.allowPersonalExpenses = allowPersonalExpenses; }
    }

    // Main class getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) { this.members = members; }

    public List<String> getAdminUsers() { return adminUsers; }
    public void setAdminUsers(List<String> adminUsers) { this.adminUsers = adminUsers; }

    public FamilyBudget getSharedBudget() { return sharedBudget; }
    public void setSharedBudget(FamilyBudget sharedBudget) { this.sharedBudget = sharedBudget; }

    public FamilySettings getSettings() { return settings; }
    public void setSettings(FamilySettings settings) { this.settings = settings; }

    // Helper methods
    public void addMember(String userId) {
        if (!members.contains(userId)) {
            members.add(userId);
        }
    }

    public void removeMember(String userId) {
        members.remove(userId);
        adminUsers.remove(userId);
    }

    public void addAdmin(String userId) {
        if (members.contains(userId) && !adminUsers.contains(userId)) {
            adminUsers.add(userId);
        }
    }

    public boolean isAdmin(String userId) {
        return adminUsers.contains(userId);
    }

    public boolean isMember(String userId) {
        return members.contains(userId);
    }

    @Override
    public String toString() {
        return "Family{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                ", members=" + members +
                ", adminUsers=" + adminUsers +
                ", sharedBudget=" + sharedBudget +
                ", settings=" + settings +
                '}';
    }
}