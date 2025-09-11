package com.dawood.finance.services.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.auth.RegisterRequestDTO;
import com.dawood.finance.dtos.auth.RegisterResponseDTO;
import com.dawood.finance.entities.User;
import com.dawood.finance.mappers.UserMapper;
import com.dawood.finance.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public RegisterResponseDTO register(RegisterRequestDTO request) {

    User newUser = User.builder()
        .email(request.getEmail())
        .fullname(request.getFullname())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

    User savedUser = userRepository.save(newUser);

    return UserMapper.toRegisterResponseDTO(savedUser);
  }

}
