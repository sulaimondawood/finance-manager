package com.dawood.finance.dtos.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExpenseRequestDTO {

  @NotEmpty(message = "Expense name is required")
  private String name;

  @NotNull(message = "Expense amount is required")
  private BigDecimal amount;

  @NotNull(message = "Expense category is required")
  private Long categoryId;

  private LocalDate date;

  private String icon;

}
