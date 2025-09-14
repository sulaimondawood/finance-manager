package com.dawood.finance.services;

import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.ApiResponse;
import com.dawood.finance.repositories.ExpenseRepository;
import com.dawood.finance.repositories.IncomeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final ExpenseRepository expenseRepository;

  private final IncomeRepository incomeRepository;

  // public ApiResponse

}
