package com.dawood.finance.mappers;

import com.dawood.finance.dtos.category.CategoryResponseDTO;
import com.dawood.finance.entities.Category;

public class CategoryMapper {

  public static CategoryResponseDTO toDTO(Category category) {
    return CategoryResponseDTO.builder()
        .id(category.getId())
        .type(category.getType())
        .name(category.getName())
        .description(category.getDescription())
        .icon(category.getIcon())
        .build();
  }

  public static Category toModel(CategoryResponseDTO cDto) {
    return Category.builder()
        .id(cDto.getId())
        .type(cDto.getType())
        .name(cDto.getName())
        .description(cDto.getDescription())
        .icon(cDto.getIcon())
        .build();
  }

}
