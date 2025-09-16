package com.dawood.finance.services;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.transaction.ExpenseResponseDTO;
import com.dawood.finance.dtos.transaction.IncomeResponseDTO;
import com.dawood.finance.dtos.transaction.TransactionResponseDTO;
import com.dawood.finance.entities.User;
import com.dawood.finance.mappers.TransactionMapper;
import com.dawood.finance.repositories.ExpenseRepository;
import com.dawood.finance.repositories.IncomeRepository;
import com.dawood.finance.services.auth.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OverviewService {

  private final AuthService authService;

  private final IncomeRepository incomeRepository;

  private final ExpenseRepository expenseRepository;

  public Map<String, Object> getTransactionOverview() {

    User user = authService.getCurrentUser();

    Map<String, Object> overiew = new LinkedHashMap<>();

    BigDecimal totalIncome = incomeRepository.findSumOfIncomesByUserId(user.getId());
    totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;

    List<IncomeResponseDTO> recentIncomes = incomeRepository.findTop5ByUserOrderByCreatedAtDesc(user).stream()
        .map(TransactionMapper::toIncomeDTO).toList();

    BigDecimal totalExpense = expenseRepository.findSumOfExpensesByUserId(user.getId());
    if (totalExpense == null)
      totalExpense = BigDecimal.ZERO;

    List<ExpenseResponseDTO> recentExpenses = expenseRepository.findTop5ByUserOrderByCreatedAtDesc(user).stream()
        .map(TransactionMapper::toExpenseDTO).toList();

    BigDecimal totalBalance = totalIncome.subtract(totalExpense);

    List<TransactionResponseDTO> allTransactions = Stream
        .concat(incomeRepository.findTop5ByUserOrderByCreatedAtDesc(user).stream()
            .map(income -> {
              return TransactionResponseDTO.builder()
                  .id(income.getId())
                  .name(income.getName())
                  .type("income")
                  .amount(income.getAmount())
                  .date(income.getDate())
                  .icon(income.getIcon())
                  .build();
            }), expenseRepository.findTop5ByUserOrderByCreatedAtDesc(user).stream()
                .map(expense -> {
                  return TransactionResponseDTO.builder()
                      .id(expense.getId())
                      .name(expense.getName())
                      .type("expense")
                      .amount(expense.getAmount())
                      .date(expense.getDate())
                      .icon(expense.getIcon())
                      .build();
                }))
        .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
        .toList();

    overiew.put("totalBalance", totalBalance);
    overiew.put("totalIncome", totalIncome);
    overiew.put("totalExpense", totalExpense);
    overiew.put("recentIncomes", recentIncomes);
    overiew.put("recentExpenses", recentExpenses);
    overiew.put("transactions", allTransactions);

    return overiew;

  }

}
