package com.dawood.finance.dtos.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IncomeRequestDTO {

  @NotEmpty(message = "Income name is required")
  private String name;

  @NotNull(message = "Income amount is required")
  private BigDecimal amount;

  @NotNull(message = "Income category is required")
  private Long categoryId;

  private LocalDate date;

  private String icon;
}
