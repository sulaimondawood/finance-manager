package com.dawood.finance.dtos.transaction;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IncomeRequestDateRangeDTO {

  @NotEmpty(message = "Income keyword is required")
  private String keyword;

  @NotNull(message = "Start date is required")
  private LocalDate startDate;

  private LocalDate endate;

}
