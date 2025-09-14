package com.dawood.finance.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.finance.dtos.ApiResponse;
import com.dawood.finance.dtos.category.CategoryRequestDTO;
import com.dawood.finance.dtos.category.CategoryResponseDTO;
import com.dawood.finance.services.CategoryService;
import com.dawood.finance.validations.CreateCategoryGroup;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  public ResponseEntity<ApiResponse<CategoryResponseDTO>> create(
      @Validated(value = { CreateCategoryGroup.class }) @RequestBody CategoryRequestDTO requestDTO) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("Category created!", categoryService.create(requestDTO)));

  }

  @PatchMapping("/{categoryId}/update")
  public ResponseEntity<ApiResponse<CategoryResponseDTO>> update(
      @RequestBody CategoryRequestDTO requestDTO, @PathVariable Long categoryId) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.success("Category updated", categoryService.update(requestDTO, categoryId)));
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<ApiResponse<CategoryResponseDTO>> getCategory(@PathVariable Long categoryId) {

    return ResponseEntity.ok()
        .body(ApiResponse.success("Category fetched successfully", categoryService.getCategory(categoryId)));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAllCategories(
      @RequestParam(defaultValue = "0", required = false) int pageNo,
      @RequestParam(defaultValue = "25", required = false) int pageSize

  ) {
    return ResponseEntity.ok().body(categoryService.getAllCategories(pageNo, pageSize));
  }

}
