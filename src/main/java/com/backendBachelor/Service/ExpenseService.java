package com.backendBachelor.Service;

import com.backendBachelor.Dto.ExpenseDto;
import com.backendBachelor.Entity.Expense;
import com.backendBachelor.Mapper.ExpenseMapper;
import com.backendBachelor.Repository.ExpenseRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService() {
        this.expenseRepository = new ExpenseRepository();
    }

    public String addExpense(ExpenseDto expenseDto) throws ExecutionException, InterruptedException {
        Expense expense = ExpenseMapper.toEntity(expenseDto);
        return expenseRepository.addExpense(expense);
    }

    public void updateExpense(ExpenseDto expenseDto) throws ExecutionException, InterruptedException {
        Expense expense = ExpenseMapper.toEntity(expenseDto);
        expenseRepository.updateExpense(expense);
    }

    public void deleteExpense(String expenseId) throws ExecutionException, InterruptedException {
        expenseRepository.deleteExpense(expenseId);
    }

    public List<ExpenseDto> getExpensesByUser(String userId) throws ExecutionException, InterruptedException {
        List<Expense> expenses = expenseRepository.getExpensesByUser(userId);
        return expenses.stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ExpenseDto> getExpensesByCategory(String userId, String category) throws ExecutionException, InterruptedException {
        List<Expense> expenses = expenseRepository.getExpensesByCategory(userId, category);
        return expenses.stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ExpenseDto> getExpensesByDateRange(String userId, LocalDateTime startDate, LocalDateTime endDate)
            throws ExecutionException, InterruptedException {
        List<Expense> expenses = expenseRepository.getExpensesByDateRange(userId, startDate, endDate);
        return expenses.stream()
                .map(ExpenseMapper::toDto)
                .collect(Collectors.toList());
    }

    public Map<String, Double> analyzeCategorySpending(String userId) throws ExecutionException, InterruptedException {
        List<Expense> expenses = expenseRepository.getExpensesByUser(userId);
        Map<String, Double> categorySpending = new HashMap<>();

        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();

            categorySpending.put(category, categorySpending.getOrDefault(category, 0.0) + amount);
        }

        return categorySpending;
    }

    public List<String> suggestBudgetCuts(String userId, double monthlyBudget) throws ExecutionException, InterruptedException {
        Map<String, Double> categorySpending = analyzeCategorySpending(userId);
        List<String> suggestions = new ArrayList<>();

        double totalSpending = categorySpending.values().stream().mapToDouble(Double::doubleValue).sum();

        if (totalSpending > monthlyBudget) {
            suggestions.add("Your total spending of $" + String.format("%.2f", totalSpending) +
                    " exceeds your monthly budget of $" + String.format("%.2f", monthlyBudget));

            // Find categories with highest spending
            categorySpending.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(3)
                    .forEach(entry -> {
                        String category = entry.getKey();
                        double amount = entry.getValue();
                        double percentage = (amount / totalSpending) * 100;

                        suggestions.add("You spent $" + String.format("%.2f", amount) +
                                " (" + String.format("%.1f", percentage) + "%) on " +
                                category + ". Consider reducing this expense.");
                    });
        } else {
            suggestions.add("Your spending is within your monthly budget. Good job!");
        }

        return suggestions;
    }


}