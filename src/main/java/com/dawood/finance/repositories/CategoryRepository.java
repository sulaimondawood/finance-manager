package com.dawood.finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.finance.entities.Category;
import com.dawood.finance.entities.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Boolean existsByNameAndUser(String name, User user);

  Category findByName(String name);

}
