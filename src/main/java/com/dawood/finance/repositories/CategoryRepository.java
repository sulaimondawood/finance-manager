package com.dawood.finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.finance.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
