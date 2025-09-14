package com.dawood.finance.dtos.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryResponseDTO {

  private Long id;

  private String name;

  private String type;

  private String icon;

  private String description;

}
