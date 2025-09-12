package com.dawood.finance.services.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.ApiResponse;
import com.dawood.finance.dtos.auth.RegisterRequestDTO;
import com.dawood.finance.dtos.auth.RegisterResponseDTO;
import com.dawood.finance.entities.User;
import com.dawood.finance.exceptions.AccountAlreadyValidatedException;
import com.dawood.finance.exceptions.EmailAlreadyExists;
import com.dawood.finance.exceptions.InvalidActivationToken;
import com.dawood.finance.exceptions.user.UserNotFoundException;
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

  @Value("${app.base-url}")
  private String baseUrl;

  public RegisterResponseDTO register(RegisterRequestDTO request) {

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new EmailAlreadyExists();
    }

    String activationToken = UUID.randomUUID().toString();

    User newUser = User.builder()
        .email(request.getEmail())
        .fullname(request.getFullname())
        .activationToken(activationToken)
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

    User savedUser = userRepository.save(newUser);

    String activationLink = baseUrl + "activate?token=" + activationToken;

    String body = "Hello " + newUser.getFullname() + "\n" + "Kindly click the link to activate your account "
        + activationLink;

    emailService.sendSimpleMail(savedUser.getEmail(), body,
        "Finance Manager account activation");

    return UserMapper.toRegisterResponseDTO(savedUser);
  }

  public String activateAccount(String token) {

    User user = userRepository.findByActivationToken(token).orElseThrow(() -> new InvalidActivationToken());

    if (user.getIsActive()) {
      throw new AccountAlreadyValidatedException("Account already validated");
    }

    user.setIsActive(true);
    user.setActivationToken(null);

    userRepository.save(user);

    return "Account activated successfully";

  }

  public boolean isAccountActive(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User does not exists"));

    return user.getIsActive();

  }
}
