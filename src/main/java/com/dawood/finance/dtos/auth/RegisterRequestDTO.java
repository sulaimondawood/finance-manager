package com.dawood.finance.dtos.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

  @NotEmpty(message = "Password is required")
  private String fullname;

  @Column(unique = true)
  private String email;

  @NotEmpty(message = "Password is required")
  @Size(min = 3, message = "Password is required")
  private String password;
}
