package com.dawood.finance.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.ApiMeta;
import com.dawood.finance.dtos.ApiResponse;
import com.dawood.finance.dtos.transaction.ExpenseRequestDTO;
import com.dawood.finance.dtos.transaction.ExpenseResponseDTO;
import com.dawood.finance.entities.Category;
import com.dawood.finance.entities.Expense;
import com.dawood.finance.entities.User;
import com.dawood.finance.exceptions.category.CategoryNotFoundException;
import com.dawood.finance.exceptions.expense.ExpenseNotFoundException;
import com.dawood.finance.mappers.TransactionMapper;
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

    return ApiResponse.success("Expense added successfully",
        TransactionMapper.toExpenseDTO(expenseRepository.save(newExpense)));

  }

  public void deleteExpense(Long expenseId) {

    User user = authService.getCurrentUser();

    Expense expense = expenseRepository.findById(expenseId)
        .orElseThrow(() -> new ExpenseNotFoundException());

    if (!user.equals(expense.getUser())) {
      throw new IllegalArgumentException("Action is not authorized");
    }

    expenseRepository.delete(expense);

  }

  public ApiResponse<ExpenseResponseDTO> updateExpense(ExpenseRequestDTO requestDTO, Long id) {
    Expense expense = expenseRepository.findByUserAndId(authService.getCurrentUser(), id)
        .orElseThrow(() -> new ExpenseNotFoundException());

    Category category = categoryRepository.findByIdAndUser(requestDTO.getCategoryId(), authService.getCurrentUser())
        .orElseThrow(() -> new CategoryNotFoundException());

    expense.setAmount(requestDTO.getAmount());
    expense.setCategory(category);
    expense.setDate(requestDTO.getDate());
    expense.setIcon(requestDTO.getIcon());
    expense.setName(requestDTO.getName());

    return ApiResponse.success("Expense updated", TransactionMapper.toExpenseDTO(expense));
  }

  public ApiResponse<List<ExpenseResponseDTO>> getUserExpenses(int pageNo, int pageSize) {

    Pageable pageable = PageRequest.of(pageNo, pageSize);

    Page<Expense> pagedExpense = expenseRepository.findByUserOrderByCreatedAtDesc(authService.getCurrentUser(),
        pageable);

    ApiMeta meta = new ApiMeta();
    meta.setHasNext(pagedExpense.hasNext());
    meta.setHasPrev(pagedExpense.hasPrevious());
    meta.setPageNo(pagedExpense.getNumber());
    meta.setPageSize(pagedExpense.getSize());
    meta.setTotalPages(pagedExpense.getTotalPages());

    List<ExpenseResponseDTO> response = pagedExpense.getContent()
        .stream()
        .map(TransactionMapper::toExpenseDTO)
        .toList();

    return ApiResponse.success("Expense fetched successfully", response, meta);

  }

  public ApiResponse<List<ExpenseResponseDTO>> getUserTop5Expenses() {

    List<Expense> expenses = expenseRepository.findTop5ByUserOrderByCreatedAtDesc(authService.getCurrentUser());

    List<ExpenseResponseDTO> response = expenses
        .stream()
        .map(TransactionMapper::toExpenseDTO)
        .toList();

    return ApiResponse.success("Expense fetched successfully", response);

  }

  public ApiResponse<ExpenseResponseDTO> getUserExpenseById(Long id) {
    Expense expense = expenseRepository.findByUserAndId(authService.getCurrentUser(), id)
        .orElseThrow(() -> new ExpenseNotFoundException());

    return ApiResponse.success("Expense retreived", TransactionMapper.toExpenseDTO(expense));
  }

  public ApiResponse<List<ExpenseResponseDTO>> getUserCurrentMonthExpenses(int pageNo, int pageSize) {

    LocalDate now = LocalDate.now();

    LocalDate startDate = now.withDayOfMonth(1);

    LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

    Pageable pageable = PageRequest.of(pageNo, pageSize);

    Page<Expense> pagedExpense = expenseRepository.findByUserAndDateBetween(authService.getCurrentUser(), startDate,
        endDate,
        pageable);

    ApiMeta meta = new ApiMeta();
    meta.setHasNext(pagedExpense.hasNext());
    meta.setHasPrev(pagedExpense.hasPrevious());
    meta.setPageNo(pagedExpense.getNumber());
    meta.setPageSize(pagedExpense.getSize());
    meta.setTotalPages(pagedExpense.getTotalPages());

    List<ExpenseResponseDTO> response = pagedExpense.getContent()
        .stream()
        .map(TransactionMapper::toExpenseDTO)
        .toList();

    return ApiResponse.success("Current month expense fetched successfully", response, meta);

  }

  public ApiResponse<List<ExpenseResponseDTO>> getUserExpensesWithDateRangeAndKeyword(int pageNo, int pageSize) {
    LocalDate now = LocalDate.now();

    LocalDate startDate = now.withDayOfMonth(1);

    LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

    Pageable pageable = PageRequest.of(pageNo, pageSize);

    Page<Expense> pagedExpense = expenseRepository.findByUserAndDateBetween(authService.getCurrentUser(), startDate,
        endDate,
        pageable);

    ApiMeta meta = new ApiMeta();
    meta.setHasNext(pagedExpense.hasNext());
    meta.setHasPrev(pagedExpense.hasPrevious());
    meta.setPageNo(pagedExpense.getNumber());
    meta.setPageSize(pagedExpense.getSize());
    meta.setTotalPages(pagedExpense.getTotalPages());

    List<ExpenseResponseDTO> response = pagedExpense.getContent()
        .stream()
        .map(TransactionMapper::toExpenseDTO)
        .toList();

    return ApiResponse.success("Expense fetched successfully", response, meta);
  }

  public ApiResponse<BigDecimal> getSumOfUserExpenses() {

    User user = authService.getCurrentUser();

    BigDecimal sumOfExpenses = expenseRepository.findSumOfExpensesByUserId(user.getId());

    BigDecimal response = sumOfExpenses != null ? sumOfExpenses : BigDecimal.ZERO;

    return ApiResponse.success("", response);
  }

}
