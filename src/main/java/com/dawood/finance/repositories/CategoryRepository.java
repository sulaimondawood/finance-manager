package com.dawood.finance.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.finance.entities.Category;
import com.dawood.finance.entities.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  boolean existsByNameAndUser(String name, User user);

  Optional<Category> findByIdAndUser(Long id, User user);

  Page<Category> findAllByUser(User user, Pageable page);

  // select * from category where user = user limit =5
  List<Category> findTop5ByUserOrderByCreatedAtDesc(User user);

}
