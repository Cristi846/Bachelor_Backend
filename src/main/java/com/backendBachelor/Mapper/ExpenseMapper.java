package com.backendBachelor.Mapper;

import com.backendBachelor.Dto.ExpenseDto;
import com.backendBachelor.Entity.Expense;

public class ExpenseMapper {

    public static ExpenseDto toDto(Expense expense) {
        if (expense == null) {
            return null;
        }

        ExpenseDto dto = new ExpenseDto();
        dto.setId(expense.getId());
        dto.setUserId(expense.getUserId());
        dto.setAmount(expense.getAmount());
        dto.setCategory(expense.getCategory());
        dto.setDescription(expense.getDescription());
        dto.setTimestamp(expense.getTimestamp());
        dto.setReceiptImageUrl(expense.getReceiptImageUrl());

        return dto;
    }

    public static Expense toEntity(ExpenseDto dto) {
        if (dto == null) {
            return null;
        }

        Expense expense = new Expense();
        expense.setId(dto.getId());
        expense.setUserId(dto.getUserId());
        expense.setAmount(dto.getAmount());
        expense.setCategory(dto.getCategory());
        expense.setDescription(dto.getDescription());
        expense.setTimestamp(dto.getTimestamp());
        expense.setReceiptImageUrl(dto.getReceiptImageUrl());

        return expense;
    }
}