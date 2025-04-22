package com.backendBachelor.Controller;

import com.backendBachelor.Dto.ExpenseDto;
import com.backendBachelor.Service.ExpenseService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController() {
        this.expenseService = new ExpenseService();
    }

    public String addExpense(ExpenseDto expenseDto) {
        try {
            return expenseService.addExpense(expenseDto);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to add expense", e);
        }
    }

    public void updateExpense(ExpenseDto expenseDto) {
        try {
            expenseService.updateExpense(expenseDto);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to update expense", e);
        }
    }

    public void deleteExpense(String expenseId) {
        try {
            expenseService.deleteExpense(expenseId);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to delete expense", e);
        }
    }

    public List<ExpenseDto> getExpensesByUser(String userId) {
        try {
            return expenseService.getExpensesByUser(userId);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get expenses", e);
        }
    }

    public List<ExpenseDto> getExpensesByCategory(String userId, String category) {
        try {
            return expenseService.getExpensesByCategory(userId, category);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get expenses by category", e);
        }
    }

    public List<ExpenseDto> getExpensesByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return expenseService.getExpensesByDateRange(userId, startDate, endDate);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get expenses by date range", e);
        }
    }

    public Map<String, Double> analyzeCategorySpending(String userId) {
        try {
            return expenseService.analyzeCategorySpending(userId);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to analyze category spending", e);
        }
    }

    public List<String> suggestBudgetCuts(String userId, double monthlyBudget) {
        try {
            return expenseService.suggestBudgetCuts(userId, monthlyBudget);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to suggest budget cuts", e);
        }
    }
}