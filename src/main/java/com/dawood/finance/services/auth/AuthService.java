package com.dawood.finance.services.auth;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.auth.RegisterRequestDTO;
import com.dawood.finance.dtos.auth.RegisterResponseDTO;
import com.dawood.finance.entities.User;
import com.dawood.finance.mappers.UserMapper;
import com.dawood.finance.repositories.UserRepository;
import com.dawood.finance.services.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  public RegisterResponseDTO register(RegisterRequestDTO request) {

    String activationToken = UUID.randomUUID().toString();

    User newUser = User.builder()
        .email(request.getEmail())
        .fullname(request.getFullname())
        .activationToken(activationToken)
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

    User savedUser = userRepository.save(newUser);

    String activationLink = "http://localhost:8080/api/v1/activate?token=" + activationToken;

    String body = "Hello" + newUser.getFullname() + "\n" + "Kindly click the link to activate your account "
        + activationLink;

    emailService.sendSimpleMail(savedUser.getEmail(), body,
        "Finance Manager account activation");

    return UserMapper.toRegisterResponseDTO(savedUser);
  }

}
