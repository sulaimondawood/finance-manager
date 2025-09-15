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
import com.dawood.finance.dtos.transaction.IncomeRequestDTO;
import com.dawood.finance.dtos.transaction.IncomeRequestDateRangeDTO;
import com.dawood.finance.dtos.transaction.IncomeResponseDTO;
import com.dawood.finance.services.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
public class IncomeController {
  private final TransactionService transactionService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<IncomeResponseDTO>>> getUserIncomes(
      @RequestParam(required = false, defaultValue = "0") int pageNo,
      @RequestParam(required = false, defaultValue = "25") int pageSize) {

    return ResponseEntity.ok().body(transactionService.getUserIncomes(pageNo, pageSize));

  }

  @GetMapping("/current-month")
  public ResponseEntity<ApiResponse<List<IncomeResponseDTO>>> getUserCurrentMonthIncomes(
      @RequestParam(required = false, defaultValue = "0") int pageNo,
      @RequestParam(required = false, defaultValue = "25") int pageSize) {

    return ResponseEntity.ok().body(transactionService.getUserCurrentMonthIncome(pageNo, pageSize));

  }

  @GetMapping("/filter")
  public ResponseEntity<ApiResponse<List<IncomeResponseDTO>>> getUserIncomesWithDateRangeAndKeyword(
      @Valid @RequestBody IncomeRequestDateRangeDTO request,
      @RequestParam(required = false, defaultValue = "0") int pageNo,
      @RequestParam(required = false, defaultValue = "25") int pageSize) {

    return ResponseEntity.ok()
        .body(transactionService.getUserIncomeWithDateRangeAndKeyword(pageNo, pageSize, request));

  }

  @PostMapping
  public ResponseEntity<ApiResponse<IncomeResponseDTO>> addIncome(
      @Valid @RequestBody IncomeRequestDTO request) {

    return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.addIncome(request));

  }

  @PatchMapping("/{incomeId}")
  public ResponseEntity<ApiResponse<IncomeResponseDTO>> updateIncome(@RequestBody IncomeRequestDTO request,
      @PathVariable Long incomeId) {

    return ResponseEntity.ok().body(transactionService.updateIncome(request, incomeId));

  }

  @DeleteMapping("/{incomeId}")
  public ResponseEntity<ApiResponse<IncomeResponseDTO>> deleteIncome(
      @PathVariable Long incomeId) {

    transactionService.deleteIncome(incomeId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success("Expense deleted"));

  }

  @GetMapping("/{incomeId}")
  public ResponseEntity<ApiResponse<IncomeResponseDTO>> getUserIncomeById(
      @PathVariable Long incomeId) {

    return ResponseEntity.ok().body(transactionService.getUserIncomeById(incomeId));

  }

  @GetMapping("/recent")
  public ResponseEntity<ApiResponse<List<IncomeResponseDTO>>> getUserTop5Incomes() {

    return ResponseEntity.ok().body(transactionService.getUserTop5Incomes());

  }

  @GetMapping("/sum")
  public ResponseEntity<ApiResponse<BigDecimal>> getSumOfIncomes() {
    return ResponseEntity.ok().body(transactionService.getSumOfUserIncomes());
  }

}
