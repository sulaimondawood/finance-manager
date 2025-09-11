package com.dawood.finance.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequestDTO {

  @NotEmpty(message = "Password is required")
  private String fullname;

  @NotEmpty(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotEmpty(message = "Password is required")
  @Size(min = 3, message = "Password is required")
  private String password;
}
