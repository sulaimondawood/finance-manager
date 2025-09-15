package com.dawood.finance.dtos.category;

import com.dawood.finance.validations.CreateCategoryGroup;

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

  @NotEmpty(message = "Category name is required", groups = { CreateCategoryGroup.class })
  private String name;

  @NotEmpty(message = "Category type is required", groups = { CreateCategoryGroup.class })
  private String type;

  private String icon;

  private String description;

}
