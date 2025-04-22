package com.backendBachelor.Dto;

import java.time.LocalDateTime;

public class ExpenseDto {
    private String id;
    private String userId;
    private double amount;
    private String category;
    private String description;
    private LocalDateTime timestamp;
    private String receiptImageUrl;

    public ExpenseDto() {
    }

    public ExpenseDto(String id, String userId, double amount, String category, String description,
                      LocalDateTime timestamp, String receiptImageUrl) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.timestamp = timestamp;
        this.receiptImageUrl = receiptImageUrl;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiptImageUrl() {
        return receiptImageUrl;
    }

    public void setReceiptImageUrl(String receiptImageUrl) {
        this.receiptImageUrl = receiptImageUrl;
    }
}