package com.dawood.finance.dtos.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IncomeRequestDTO {
  @NotEmpty(message = "Expense name is required")
  private String name;

  @NotEmpty(message = "Expense amount is required")
  private BigDecimal amount;

  private LocalDate date;

  private String icon;
}
