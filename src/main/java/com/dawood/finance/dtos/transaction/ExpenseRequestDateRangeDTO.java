package com.dawood.finance.dtos.transaction;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExpenseRequestDateRangeDTO {

  @NotEmpty(message = "Expense keyword is required")
  private String keyword;

  @NotNull(message = "Start date is required")
  private LocalDate startDate;

  private LocalDate endate;

}
