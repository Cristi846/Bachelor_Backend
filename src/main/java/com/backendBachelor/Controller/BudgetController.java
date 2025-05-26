package com.backendBachelor.Controller;

import com.backendBachelor.Service.BudgetService;
import com.backendBachelor.Service.ExpenseService;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BudgetController {
    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    public BudgetController() {
        this.budgetService = new BudgetService();
        this.expenseService = new ExpenseService();
    }

    /**
     * Set budget for a specific category
     */
    public void setCategoryBudget(String userId, String category, double amount) {
        try {
            budgetService.setCategoryBudget(userId, category, amount);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to set category budget", e);
        }
    }

    /**
     * Get budget for a specific category
     */
    public double getCategoryBudget(String userId, String category) {
        try {
            return budgetService.getCategoryBudget(userId, category);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get category budget", e);
        }
    }

    /**
     * Get all category budgets for a user
     */
    public Map<String, Double> getAllCategoryBudgets(String userId) {
        try {
            return budgetService.getAllCategoryBudgets(userId);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get category budgets", e);
        }
    }

    /**
     * Analyze spending against budgets by category
     */
    public Map<String, Map<String, Double>> analyzeSpendingVsBudget(String userId) {
        try {
            return budgetService.analyzeSpendingVsBudget(userId);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to analyze spending vs budget", e);
        }
    }

    /**
     * Get categories that are over budget
     */
    public Map<String, Double> getOverBudgetCategories(String userId) {
        try {
            return budgetService.getOverBudgetCategories(userId);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get over budget categories", e);
        }
    }

    /**
     * Get budget suggestions based on spending patterns
     */
    public Map<String, String> getBudgetSuggestions(String userId) {
        try {
            return budgetService.generateCategoryBudgetSuggestions(userId);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to generate budget suggestions", e);
        }
    }
}