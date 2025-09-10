package com.dawood.finance.services.auth;

import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.auth.RegisterRequestDTO;
import com.dawood.finance.dtos.auth.RegisterResponseDTO;
import com.dawood.finance.entities.User;
import com.dawood.finance.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  public RegisterResponseDTO register(RegisterResponseDTO request) {

    // User newUser = User.builder()
    // .email(request.getEmail())
    // .fullname(request.getFullname())

    return null;
  }

}
