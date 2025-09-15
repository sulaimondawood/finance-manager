package com.dawood.finance.mappers;

import com.dawood.finance.dtos.transaction.ExpenseResponseDTO;
import com.dawood.finance.dtos.transaction.IncomeResponseDTO;
import com.dawood.finance.entities.Expense;
import com.dawood.finance.entities.Income;

public class TransactionMapper {

  public static ExpenseResponseDTO toExpenseDTO(Expense expense) {
    return ExpenseResponseDTO.builder()
        .id(expense.getId())
        .name(expense.getName())
        .amount(expense.getAmount())
        .date(expense.getDate())
        .icon(expense.getIcon())
        .updatedAt(expense.getUpdatedAt())
        .createdAt(expense.getCreatedAt())
        .build();
  }

  public static IncomeResponseDTO toIncomeDTO(Income income) {
    return IncomeResponseDTO.builder()
        .id(income.getId())
        .name(income.getName())
        .amount(income.getAmount())
        .date(income.getDate())
        .icon(income.getIcon())
        .updatedAt(income.getUpdatedAt())
        .createdAt(income.getCreatedAt())
        .build();
  }

}
