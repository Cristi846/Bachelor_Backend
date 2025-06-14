package com.backendBachelor.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class FamilyInvitation {
    private String id;
    private String familyId;
    private String familyName;
    private String invitedBy;
    private String invitedByName;
    private String invitedEmail;
    private String invitedUserId; // Set when user accepts invitation
    private InvitationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime respondedAt;

    public enum InvitationStatus {
        PENDING,
        ACCEPTED,
        DECLINED,
        EXPIRED,
        CANCELLED
    }

    // Default constructor for Firebase
    public FamilyInvitation() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusDays(7); // Expires in 7 days
        this.status = InvitationStatus.PENDING;
    }

    // Constructor with parameters
    public FamilyInvitation(String familyId, String familyName, String invitedBy,
                            String invitedByName, String invitedEmail) {
        this();
        this.familyId = familyId;
        this.familyName = familyName;
        this.invitedBy = invitedBy;
        this.invitedByName = invitedByName;
        this.invitedEmail = invitedEmail;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFamilyId() { return familyId; }
    public void setFamilyId(String familyId) { this.familyId = familyId; }

    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }

    public String getInvitedBy() { return invitedBy; }
    public void setInvitedBy(String invitedBy) { this.invitedBy = invitedBy; }

    public String getInvitedByName() { return invitedByName; }
    public void setInvitedByName(String invitedByName) { this.invitedByName = invitedByName; }

    public String getInvitedEmail() { return invitedEmail; }
    public void setInvitedEmail(String invitedEmail) { this.invitedEmail = invitedEmail; }

    public String getInvitedUserId() { return invitedUserId; }
    public void setInvitedUserId(String invitedUserId) { this.invitedUserId = invitedUserId; }

    public InvitationStatus getStatus() { return status; }
    public void setStatus(InvitationStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }

    // Helper methods
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isPending() {
        return status == InvitationStatus.PENDING && !isExpired();
    }

    public void accept(String userId) {
        this.invitedUserId = userId;
        this.status = InvitationStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void decline() {
        this.status = InvitationStatus.DECLINED;
        this.respondedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = InvitationStatus.CANCELLED;
        this.respondedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "FamilyInvitation{" +
                "id='" + id + '\'' +
                ", familyId='" + familyId + '\'' +
                ", familyName='" + familyName + '\'' +
                ", invitedBy='" + invitedBy + '\'' +
                ", invitedByName='" + invitedByName + '\'' +
                ", invitedEmail='" + invitedEmail + '\'' +
                ", invitedUserId='" + invitedUserId + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", respondedAt=" + respondedAt +
                '}';
    }
}