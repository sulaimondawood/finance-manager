package com.dawood.finance.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.finance.dtos.ApiResponse;
import com.dawood.finance.dtos.transaction.ExpenseRequestDTO;
import com.dawood.finance.dtos.transaction.ExpenseRequestDateRangeDTO;
import com.dawood.finance.dtos.transaction.ExpenseResponseDTO;
import com.dawood.finance.services.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

  private final TransactionService transactionService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<ExpenseResponseDTO>>> getUserExpenses(
      @RequestParam(required = false, defaultValue = "0") int pageNo,
      @RequestParam(required = false, defaultValue = "25") int pageSize) {

    return ResponseEntity.ok().body(transactionService.getUserExpenses(pageNo, pageSize));

  }

  @GetMapping("/current-month")
  public ResponseEntity<ApiResponse<List<ExpenseResponseDTO>>> getUserCurrentMonthExpenses(
      @RequestParam(required = false, defaultValue = "0") int pageNo,
      @RequestParam(required = false, defaultValue = "25") int pageSize) {

    return ResponseEntity.ok().body(transactionService.getUserCurrentMonthExpenses(pageNo, pageSize));

  }

  @GetMapping("/filter")
  public ResponseEntity<ApiResponse<List<ExpenseResponseDTO>>> getUserExpensesWithDateRangeAndKeyword(
      @Valid @RequestBody ExpenseRequestDateRangeDTO request,
      @RequestParam(required = false, defaultValue = "0") int pageNo,
      @RequestParam(required = false, defaultValue = "25") int pageSize) {

    return ResponseEntity.ok()
        .body(transactionService.getUserExpensesWithDateRangeAndKeyword(pageNo, pageSize, request));

  }

  @PostMapping
  public ResponseEntity<ApiResponse<ExpenseResponseDTO>> addExpense(
      @Valid @RequestBody ExpenseRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.addExpense(request));

  }

  @PatchMapping("/{expenseId}")
  public ResponseEntity<ApiResponse<ExpenseResponseDTO>> updateExpense(@RequestBody ExpenseRequestDTO request,
      @PathVariable Long expenseId) {

    return ResponseEntity.ok().body(transactionService.updateExpense(request, expenseId));

  }

  @DeleteMapping("/{expenseId}")
  public ResponseEntity<ApiResponse<ExpenseResponseDTO>> deleteExpense(
      @PathVariable Long expenseId) {

    transactionService.deleteExpense(expenseId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("Expense deleted"));

  }

  @GetMapping("/{expenseId}")
  public ResponseEntity<ApiResponse<ExpenseResponseDTO>> getUserExpenseById(
      @PathVariable Long expenseId) {

    return ResponseEntity.ok().body(transactionService.getUserExpenseById(expenseId));

  }

  @GetMapping("/recent")
  public ResponseEntity<ApiResponse<List<ExpenseResponseDTO>>> getUserTop5Expenses() {

    return ResponseEntity.ok().body(transactionService.getUserTop5Expenses());

  }

  @GetMapping("/sum")
  public ResponseEntity<ApiResponse<BigDecimal>> getSumOfExpenses() {
    return ResponseEntity.ok().body(transactionService.getSumOfUserExpenses());
  }

}
