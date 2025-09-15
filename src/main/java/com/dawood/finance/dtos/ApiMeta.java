package com.dawood.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiMeta {

  private int pageNo;

  private int pageSize;

  private long totalPages;

  private boolean hasPrev;

  private boolean hasNext;

}
