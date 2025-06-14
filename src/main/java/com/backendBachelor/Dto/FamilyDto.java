package com.backendBachelor.Dto;

import com.backendBachelor.Entity.Family;
import com.backendBachelor.Entity.FamilyInvitation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// Family DTOs
public class FamilyDto {
    private String id;
    private String name;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<String> members;
    private List<String> adminUsers;
    private Family.FamilyBudget sharedBudget;
    private Family.FamilySettings settings;

    public FamilyDto() {}

    // Getters and Setters
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

    public Family.FamilyBudget getSharedBudget() { return sharedBudget; }
    public void setSharedBudget(Family.FamilyBudget sharedBudget) { this.sharedBudget = sharedBudget; }

    public Family.FamilySettings getSettings() { return settings; }
    public void setSettings(Family.FamilySettings settings) { this.settings = settings; }
}

