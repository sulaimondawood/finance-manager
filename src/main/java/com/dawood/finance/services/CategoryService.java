package com.dawood.finance.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.ApiMeta;
import com.dawood.finance.dtos.ApiResponse;
import com.dawood.finance.dtos.category.CategoryRequestDTO;
import com.dawood.finance.dtos.category.CategoryResponseDTO;
import com.dawood.finance.entities.Category;
import com.dawood.finance.entities.User;
import com.dawood.finance.exceptions.category.CategoryNotFoundException;
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

    User currentUser = authService.getCurrentUser();

    if (categoryRepository.existsByNameAndUser(requestDTO.getName(), currentUser)) {
      throw new IllegalArgumentException("Category with name " + requestDTO.getName() + " already exists");
    }

    Category category = Category.builder()
        .name(requestDTO.getName())
        .type(requestDTO.getType())
        .description(requestDTO.getDescription())
        .icon(requestDTO.getIcon())
        .user(currentUser)
        .build();

    return CategoryMapper.toDTO(categoryRepository.save(category));

  }

  public CategoryResponseDTO update(CategoryRequestDTO requestDTO, Long id) {

    Category category = CategoryMapper.toModel(getCategory(id));

    category.setName(requestDTO.getName());
    category.setType(requestDTO.getType());
    category.setIcon(requestDTO.getIcon());
    category.setDescription(requestDTO.getDescription());

    return CategoryMapper.toDTO(categoryRepository.save(category));

  }

  public CategoryResponseDTO getCategory(Long id) {
    Category category = categoryRepository.findByIdAndUser(id, authService.getCurrentUser())
        .orElseThrow(() -> new CategoryNotFoundException());

    return CategoryMapper.toDTO(category);
  }

  public ApiResponse<List<CategoryResponseDTO>> getAllCategories(int pageNo, int pageSize) {
    Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());

    Page<Category> pagedCategory = categoryRepository.findAllByUser(authService.getCurrentUser(), pageable);

    ApiMeta meta = new ApiMeta();

    meta.setPageNo(pagedCategory.getNumber());
    meta.setPageSize(pagedCategory.getSize());
    meta.setTotalPages(pagedCategory.getTotalPages());
    meta.setHasPrev(pagedCategory.hasPrevious());
    meta.setHasNext(pagedCategory.hasNext());

    List<CategoryResponseDTO> categoryResponseDTOs = pagedCategory.getContent().stream()
        .map(CategoryMapper::toDTO).toList();

    return ApiResponse.success("Categories fetched successfully", categoryResponseDTOs);

  }
}
