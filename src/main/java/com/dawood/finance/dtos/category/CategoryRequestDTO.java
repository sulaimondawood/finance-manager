package com.dawood.finance.dtos.category;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

  @NotEmpty(message = "Category name is required")
  private String name;

  private String icon;

  private String description;

}
