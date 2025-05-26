package com.backendBachelor.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String id;
    private String email;
    private String name;
    private double monthlyBudget;
    private List<String> expenseCategories;
    private Map<String, Double> categoryBudgets;

    // Default constructor for Firebase
    public User() {
        this.expenseCategories = new ArrayList<>();
        this.categoryBudgets = new HashMap<>();
        initDefaultCategories();
    }

    // Constructor with parameters
    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.monthlyBudget = 0.0;
        this.expenseCategories = new ArrayList<>();
        this.categoryBudgets = new HashMap<>();
        initDefaultCategories();
    }

    private void initDefaultCategories() {
        // Add default categories
        expenseCategories.add("Food");
        expenseCategories.add("Transportation");
        expenseCategories.add("Housing");
        expenseCategories.add("Entertainment");
        expenseCategories.add("Utilities");
        expenseCategories.add("Healthcare");
        expenseCategories.add("Shopping");
        expenseCategories.add("Other");

        // Initialize default category budgets with 0.0
        for (String category : expenseCategories) {
            categoryBudgets.put(category, 0.0);
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public List<String> getExpenseCategories() {
        return expenseCategories;
    }

    public void setExpenseCategories(List<String> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    public Map<String, Double> getCategoryBudgets() {
        return categoryBudgets;
    }

    public void setCategoryBudgets(Map<String, Double> categoryBudgets) {
        this.categoryBudgets = categoryBudgets;
    }

    public void setCategoryBudget(String category, double budget) {
        this.categoryBudgets.put(category, budget);
    }

    public double getCategoryBudget(String category) {
        return this.categoryBudgets.getOrDefault(category, 0.0);
    }

    public void addExpenseCategory(String category) {
        if (!expenseCategories.contains(category)) {
            expenseCategories.add(category);
            categoryBudgets.put(category, 0.0);
        }
    }

    public void removeExpenseCategory(String category) {
        expenseCategories.remove(category);
        categoryBudgets.remove(category);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", monthlyBudget=" + monthlyBudget +
                ", expenseCategories=" + expenseCategories +
                ", categoryBudgets=" + categoryBudgets +
                '}';
    }
}