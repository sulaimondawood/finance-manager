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

import com.dawood.finance.entities.Income;
import com.dawood.finance.entities.User;

public interface IncomeRepository extends JpaRepository<Income, Long> {
  Page<Income> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

  List<Income> findByUser(User user);

  List<Income> findTop5ByUserOrderByCreatedAtDesc(User user);

  @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id=:userId")
  BigDecimal findSumOfIncomesByUserId(@Param("userId") Long userId);

  Page<Income> findByUserAndDateBetweenAndNameContainingIgnoreCase(User user, LocalDate startDate, LocalDate endDate,
      String keyword, Pageable pageable);

  Page<Income> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate enDate, Pageable pageable);

  Optional<Income> findByUserAndId(User user, Long id);

}
