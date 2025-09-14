package com.dawood.finance.services;

import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.ApiResponse;
import com.dawood.finance.dtos.transaction.ExpenseRequestDTO;
import com.dawood.finance.dtos.transaction.ExpenseResponseDTO;
import com.dawood.finance.entities.Category;
import com.dawood.finance.entities.Expense;
import com.dawood.finance.entities.User;
import com.dawood.finance.exceptions.category.CategoryNotFoundException;
import com.dawood.finance.repositories.CategoryRepository;
import com.dawood.finance.repositories.ExpenseRepository;
import com.dawood.finance.repositories.IncomeRepository;
import com.dawood.finance.services.auth.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final ExpenseRepository expenseRepository;

  private final IncomeRepository incomeRepository;

  private final CategoryRepository categoryRepository;

  private final AuthService authService;

  public ApiResponse<ExpenseResponseDTO> addExpense(ExpenseRequestDTO request) {

    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new CategoryNotFoundException());

    User user = authService.getCurrentUser();

    Expense newExpense = new Expense();

    newExpense.setName(request.getName());
    newExpense.setAmount(request.getAmount());
    newExpense.setDate(request.getDate());
    newExpense.setIcon(request.getIcon());
    newExpense.setUser(user);
    newExpense.setCategory(category);

    return null;

  }

}
