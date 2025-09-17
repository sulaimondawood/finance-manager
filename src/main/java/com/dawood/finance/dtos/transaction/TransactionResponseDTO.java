package com.dawood.finance.dtos.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponseDTO {
  private Long id;

  private String name;

  private String type;

  private BigDecimal amount;

  private LocalDate date;

  private String icon;
}
