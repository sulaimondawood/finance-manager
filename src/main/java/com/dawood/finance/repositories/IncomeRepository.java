package com.dawood.finance.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dawood.finance.entities.Expense;
import com.dawood.finance.entities.Income;
import com.dawood.finance.entities.User;

public interface IncomeRepository extends JpaRepository<Income, Long> {
  Page<Expense> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

  List<Expense> findTop5ByUserOrderByCreatedAtDesc(User user);

  @Query("SELECT SUM(i.amount) FROM Expense i WHERE i.user.id=:userId")
  BigDecimal findSumOfExpensesByUserId(@Param("userId") String userId);

  Page<Expense> findByUserAndDateBetweenAndNameContainingIgnoreCase(User user, LocalDate startDate, LocalDate endDate,
      String keyword, Pageable pageable);

  Page<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate enDate, Pageable pageable);

}
