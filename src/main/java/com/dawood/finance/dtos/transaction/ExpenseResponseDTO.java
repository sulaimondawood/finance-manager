package com.dawood.finance.dtos.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponseDTO {

  private Long id;

  private String name;

  private BigDecimal amount;

  private LocalDate date;

  private String icon;

  private LocalDateTime updatedAt;

  private LocalDateTime createdAt;

}
