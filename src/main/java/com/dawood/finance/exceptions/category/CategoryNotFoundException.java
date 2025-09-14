package com.dawood.finance.exceptions.category;

public class CategoryNotFoundException extends RuntimeException {

  public CategoryNotFoundException() {
    super("Category not found");
  }

  public CategoryNotFoundException(String message) {
    super(message);
  }

}
