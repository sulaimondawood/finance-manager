package com.dawood.finance.services;

import org.springframework.stereotype.Service;

import com.dawood.finance.repositories.CategoryRepository;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

}
