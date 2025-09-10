package com.dawood.finance.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.finance.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

}
