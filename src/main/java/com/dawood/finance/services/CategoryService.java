package com.dawood.finance.services;

import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.category.CategoryRequestDTO;
import com.dawood.finance.dtos.category.CategoryResponseDTO;
import com.dawood.finance.entities.Category;
import com.dawood.finance.mappers.CategoryMapper;
import com.dawood.finance.repositories.CategoryRepository;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public CategoryResponseDTO create(CategoryRequestDTO requestDTO) {

    if (categoryRepository.existsByName(requestDTO.getName())) {
      throw new IllegalArgumentException("Category with name " + requestDTO.getName() + " already exists");
    }

    Category category = Category.builder()
        .name(requestDTO.getName())
        .descriprion(requestDTO.getDescriprion())
        .icon(requestDTO.getIcon())
        .build();

    return CategoryMapper.toDTO(categoryRepository.save(category));

  }

}
