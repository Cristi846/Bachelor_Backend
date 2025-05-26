package com.backendBachelor.Service;

import com.backendBachelor.Entity.User;
import com.backendBachelor.Repository.UserRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BudgetService {
    private final UserRepository userRepository;
    private final ExpenseService expenseService;

    public BudgetService() {
        this.userRepository = new UserRepository();
        this.expenseService = new ExpenseService();
    }

    /**
     * Get a user's budget for a specific category
     */
    public double getCategoryBudget(String userId, String category) throws ExecutionException, InterruptedException {
        User user = userRepository.getUserById(userId);
        if (user != null) {
            return user.getCategoryBudget(category);
        }
        return 0.0;
    }

    /**
     * Set a budget for a specific category
     */
    public void setCategoryBudget(String userId, String category, double amount) throws ExecutionException, InterruptedException {
        User user = userRepository.getUserById(userId);
        if (user != null) {
            user.setCategoryBudget(category, amount);
            userRepository.updateUser(user);
        }
    }

    /**
     * Get all category budgets for a user
     */
    public Map<String, Double> getAllCategoryBudgets(String userId) throws ExecutionException, InterruptedException {
        User user = userRepository.getUserById(userId);
        if (user != null) {
            return user.getCategoryBudgets();
        }
        return new HashMap<>();
    }

    /**
     * Calculate spending vs budget for each category
     */
    public Map<String, Map<String, Double>> analyzeSpendingVsBudget(String userId) throws ExecutionException, InterruptedException {
        Map<String, Double> spending = expenseService.analyzeCategorySpending(userId);
        Map<String, Double> budgets = getAllCategoryBudgets(userId);

        Map<String, Map<String, Double>> analysis = new HashMap<>();

        for (String category : budgets.keySet()) {
            double budget = budgets.getOrDefault(category, 0.0);
            double spent = spending.getOrDefault(category, 0.0);
            double remaining = budget - spent;
            double percentUsed = budget > 0 ? (spent / budget) * 100 : 0;

            Map<String, Double> categoryAnalysis = new HashMap<>();
            categoryAnalysis.put("budget", budget);
            categoryAnalysis.put("spent", spent);
            categoryAnalysis.put("remaining", remaining);
            categoryAnalysis.put("percentUsed", percentUsed);

            analysis.put(category, categoryAnalysis);
        }

        return analysis;
    }

    /**
     * Get categories that are over budget
     */
    public Map<String, Double> getOverBudgetCategories(String userId) throws ExecutionException, InterruptedException {
        Map<String, Double> spending = expenseService.analyzeCategorySpending(userId);
        Map<String, Double> budgets = getAllCategoryBudgets(userId);
        Map<String, Double> overBudgetCategories = new HashMap<>();

        for (String category : budgets.keySet()) {
            double budget = budgets.getOrDefault(category, 0.0);
            if (budget > 0) {  // Only consider categories with actual budgets set
                double spent = spending.getOrDefault(category, 0.0);

                if (spent > budget) {
                    overBudgetCategories.put(category, spent - budget);
                }
            }
        }

        return overBudgetCategories;
    }

    /**
     * Generate specific budget suggestions based on category spending
     */
    public Map<String, String> generateCategoryBudgetSuggestions(String userId) throws ExecutionException, InterruptedException {
        Map<String, Double> spending = expenseService.analyzeCategorySpending(userId);
        Map<String, Double> budgets = getAllCategoryBudgets(userId);
        Map<String, String> suggestions = new HashMap<>();

        // First, identify categories without budgets
        for (String category : spending.keySet()) {
            double spent = spending.get(category);
            double budget = budgets.getOrDefault(category, 0.0);

            if (budget == 0.0 && spent > 0) {
                // Suggest setting a budget based on current spending
                suggestions.put(category, "Consider setting a budget for " + category +
                        " based on your average spending of $" +
                        String.format("%.2f", spent));
            } else if (spent > budget && budget > 0) {
                // Category is over budget
                double overage = spent - budget;
                double percentOver = (overage / budget) * 100;

                suggestions.put(category, "Your " + category + " spending exceeds your budget by $" +
                        String.format("%.2f", overage) + " (" +
                        String.format("%.1f", percentOver) + "%). Consider increasing your budget or reducing expenses.");
            } else if (spent < budget * 0.5 && budget > 0) {
                // Category is significantly under budget
                suggestions.put(category, "You're using only " +
                        String.format("%.1f", (spent / budget) * 100) +
                        "% of your " + category + " budget. Consider reallocating some funds to other categories.");
            }
        }

        return suggestions;
    }
}