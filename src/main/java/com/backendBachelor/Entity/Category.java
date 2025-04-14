package com.backendBachelor.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Category {
    public enum CategoryType{
        INCOME, EXPENSE
    }

    private String id;
    private String userId;
    private String name;
    private String color;
    private String icon;
    private CategoryType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Category(String userId, String name, String color, String icon, CategoryType type) {
        this();
        this.userId = userId;
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.type = type;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
