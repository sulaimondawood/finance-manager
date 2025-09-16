package com.dawood.finance.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dawood.finance.entities.Expense;
import com.dawood.finance.entities.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  Page<Expense> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

  List<Expense> findByUser(User user);

  List<Expense> findTop5ByUserOrderByCreatedAtDesc(User user);

  Optional<Expense> findByUserAndId(User user, Long id);

  @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id=:userId")
  BigDecimal findSumOfExpensesByUserId(@Param("userId") Long userId);

  Page<Expense> findByUserAndDateBetweenAndNameContainingIgnoreCase(User user, LocalDate startDate, LocalDate endDate,
      String keyword, Pageable pageable);

  Page<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate enDate, Pageable pageable);
}
