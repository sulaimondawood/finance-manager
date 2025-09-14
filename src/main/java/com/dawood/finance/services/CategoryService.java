package com.dawood.finance.services;

import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.category.CategoryRequestDTO;
import com.dawood.finance.dtos.category.CategoryResponseDTO;
import com.dawood.finance.entities.Category;
import com.dawood.finance.mappers.CategoryMapper;
import com.dawood.finance.repositories.CategoryRepository;
import com.dawood.finance.services.auth.AuthService;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final AuthService authService;

  public CategoryService(CategoryRepository categoryRepository, AuthService authService) {
    this.categoryRepository = categoryRepository;
    this.authService = authService;
  }

  public CategoryResponseDTO create(CategoryRequestDTO requestDTO) {

    if (categoryRepository.existsByNameAndUser(requestDTO.getName(), authService.getCurrentUser())) {
      throw new IllegalArgumentException("Category with name " + requestDTO.getName() + " already exists");
    }

    Category category = Category.builder()
        .name(requestDTO.getName())
        .description(requestDTO.getDescription())
        .icon(requestDTO.getIcon())
        .build();

    return CategoryMapper.toDTO(categoryRepository.save(category));

  }

}
