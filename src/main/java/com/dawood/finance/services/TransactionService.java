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
import com.dawood.finance.dtos.transaction.ExpenseRequestDateRangeDTO;
import com.dawood.finance.dtos.transaction.ExpenseResponseDTO;
import com.dawood.finance.dtos.transaction.IncomeRequestDTO;
import com.dawood.finance.dtos.transaction.IncomeRequestDateRangeDTO;
import com.dawood.finance.dtos.transaction.IncomeResponseDTO;
import com.dawood.finance.entities.Category;
import com.dawood.finance.entities.Expense;
import com.dawood.finance.entities.Income;
import com.dawood.finance.entities.User;
import com.dawood.finance.exceptions.category.CategoryNotFoundException;
import com.dawood.finance.exceptions.expense.ExpenseNotFoundException;
import com.dawood.finance.exceptions.income.IncomeNotFoundException;
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

    expense.setAmount(requestDTO.getAmount());
    expense.setDate(requestDTO.getDate());
    expense.setIcon(requestDTO.getIcon());
    expense.setName(requestDTO.getName());

    Expense savedExpense = expenseRepository.save(expense);
    return ApiResponse.success("Expense updated", TransactionMapper.toExpenseDTO(savedExpense));
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

  public ApiResponse<List<ExpenseResponseDTO>> getUserExpensesWithDateRangeAndKeyword(int pageNo, int pageSize,
      ExpenseRequestDateRangeDTO request) {

    Pageable pageable = PageRequest.of(pageNo, pageSize);

    LocalDate startDate = request.getStartDate();
    LocalDate endDate = request.getEndate();

    if (request.getEndate() == null) {
      endDate = LocalDate.now().withMonth(12).withDayOfMonth(31);
    }

    Page<Expense> pagedExpense = expenseRepository.findByUserAndDateBetweenAndNameContainingIgnoreCase(
        authService.getCurrentUser(), startDate,
        endDate,
        request.getKeyword(),
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

  /*
   * Income service implementation
   * Section
   */

  public ApiResponse<IncomeResponseDTO> addIncome(IncomeRequestDTO request) {

    Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new CategoryNotFoundException());

    User user = authService.getCurrentUser();

    Income newIncome = new Income();

    newIncome.setName(request.getName());
    newIncome.setAmount(request.getAmount());
    newIncome.setDate(request.getDate());
    newIncome.setIcon(request.getIcon());
    newIncome.setUser(user);
    newIncome.setCategory(category);

    return ApiResponse.success("Income added successfully",
        TransactionMapper.toIncomeDTO(incomeRepository.save(newIncome)));

  }

  public void deleteIncome(Long incomeId) {

    User user = authService.getCurrentUser();

    Income income = incomeRepository.findById(incomeId)
        .orElseThrow(() -> new IncomeNotFoundException());

    if (!user.equals(income.getUser())) {
      throw new IllegalArgumentException("Action is not authorized");
    }

    incomeRepository.delete(income);

  }

  public ApiResponse<IncomeResponseDTO> updateIncome(IncomeRequestDTO requestDTO, Long id) {
    Income income = incomeRepository.findByUserAndId(authService.getCurrentUser(), id)
        .orElseThrow(() -> new IncomeNotFoundException());

    income.setAmount(requestDTO.getAmount());
    income.setDate(requestDTO.getDate());
    income.setIcon(requestDTO.getIcon());
    income.setName(requestDTO.getName());

    Income savedincome = incomeRepository.save(income);
    return ApiResponse.success("Income updated", TransactionMapper.toIncomeDTO(savedincome));
  }

  public ApiResponse<List<IncomeResponseDTO>> getUserIncomes(int pageNo, int pageSize) {

    Pageable pageable = PageRequest.of(pageNo, pageSize);

    Page<Income> pagedIncome = incomeRepository.findByUserOrderByCreatedAtDesc(authService.getCurrentUser(),
        pageable);

    ApiMeta meta = new ApiMeta();
    meta.setHasNext(pagedIncome.hasNext());
    meta.setHasPrev(pagedIncome.hasPrevious());
    meta.setPageNo(pagedIncome.getNumber());
    meta.setPageSize(pagedIncome.getSize());
    meta.setTotalPages(pagedIncome.getTotalPages());

    List<IncomeResponseDTO> response = pagedIncome.getContent()
        .stream()
        .map(TransactionMapper::toIncomeDTO)
        .toList();

    return ApiResponse.success("Income fetched successfully", response, meta);

  }

  public ApiResponse<List<IncomeResponseDTO>> getUserTop5Incomes() {

    List<Income> incomes = incomeRepository.findTop5ByUserOrderByCreatedAtDesc(authService.getCurrentUser());

    List<IncomeResponseDTO> response = incomes
        .stream()
        .map(TransactionMapper::toIncomeDTO)
        .toList();

    return ApiResponse.success("Income fetched successfully", response);

  }

  public ApiResponse<IncomeResponseDTO> getUserIncomeById(Long id) {
    Income income = incomeRepository.findByUserAndId(authService.getCurrentUser(), id)
        .orElseThrow(() -> new IncomeNotFoundException());

    return ApiResponse.success("Income retreived", TransactionMapper.toIncomeDTO(income));
  }

  public ApiResponse<List<IncomeResponseDTO>> getUserCurrentMonthIncome(int pageNo, int pageSize) {

    LocalDate now = LocalDate.now();

    LocalDate startDate = now.withDayOfMonth(1);

    LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

    Pageable pageable = PageRequest.of(pageNo, pageSize);

    Page<Income> pagedIncome = incomeRepository.findByUserAndDateBetween(authService.getCurrentUser(), startDate,
        endDate,
        pageable);

    ApiMeta meta = new ApiMeta();
    meta.setHasNext(pagedIncome.hasNext());
    meta.setHasPrev(pagedIncome.hasPrevious());
    meta.setPageNo(pagedIncome.getNumber());
    meta.setPageSize(pagedIncome.getSize());
    meta.setTotalPages(pagedIncome.getTotalPages());

    List<IncomeResponseDTO> response = pagedIncome.getContent()
        .stream()
        .map(TransactionMapper::toIncomeDTO)
        .toList();

    return ApiResponse.success("Current month income fetched successfully", response, meta);

  }

  public ApiResponse<List<IncomeResponseDTO>> getUserIncomeWithDateRangeAndKeyword(int pageNo, int pageSize,
      IncomeRequestDateRangeDTO request) {

    Pageable pageable = PageRequest.of(pageNo, pageSize);

    LocalDate startDate = request.getStartDate();
    LocalDate endDate = request.getEndate();

    if (request.getEndate() == null) {
      endDate = LocalDate.now().withMonth(12).withDayOfMonth(31);
    }

    Page<Income> pagedIncome = incomeRepository.findByUserAndDateBetweenAndNameContainingIgnoreCase(
        authService.getCurrentUser(), startDate,
        endDate,
        request.getKeyword(),
        pageable);

    ApiMeta meta = new ApiMeta();
    meta.setHasNext(pagedIncome.hasNext());
    meta.setHasPrev(pagedIncome.hasPrevious());
    meta.setPageNo(pagedIncome.getNumber());
    meta.setPageSize(pagedIncome.getSize());
    meta.setTotalPages(pagedIncome.getTotalPages());

    List<IncomeResponseDTO> response = pagedIncome.getContent()
        .stream()
        .map(TransactionMapper::toIncomeDTO)
        .toList();

    return ApiResponse.success("Income fetched successfully", response, meta);
  }

  public ApiResponse<BigDecimal> getSumOfUserIncomes() {

    User user = authService.getCurrentUser();

    BigDecimal sumOfIncomes = incomeRepository.findSumOfIncomesByUserId(user.getId());

    BigDecimal response = sumOfIncomes != null ? sumOfIncomes : BigDecimal.ZERO;

    return ApiResponse.success("", response);
  }

}
