package com.dawood.finance.services.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dawood.finance.dtos.auth.LoginRequest;
import com.dawood.finance.dtos.auth.LoginResponse;
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
import com.dawood.finance.services.UserDetailsServiceImpl;
import com.dawood.finance.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final JwtUtils jwtUtils;
  private final UserDetailsServiceImpl userDetailsServiceImpl;

  String activationToken = UUID.randomUUID().toString();

  @Value("${app.base-url}")
  private String baseUrl;

  public RegisterResponseDTO register(RegisterRequestDTO request) {

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new EmailAlreadyExists();
    }

    User newUser = User.builder()
        .email(request.getEmail())
        .fullname(request.getFullname())
        .activationToken(activationToken)
        .password(passwordEncoder.encode(request.getPassword()))
        .build();

    User savedUser = userRepository.save(newUser);

    sendMail(savedUser);

    return UserMapper.toRegisterResponseDTO(savedUser);
  }

  public LoginResponse login(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

    if (!isAccountActive(user.getEmail())) {
      sendMail(user);
      return LoginResponse.builder().message("Activation link sent to your email").build();
    }

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BadCredentialsException("Invalid email or password");
    }

    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(request.getEmail());

    String token = jwtUtils.generateToken(userDetails);

    return LoginResponse.builder().token(token).message("Login successfull").build();

  }

  public String activateAccount(String token) {

    User user = userRepository.findByActivationToken(token).orElseThrow(() -> new InvalidActivationToken());

    if (user.getIsActive()) {
      throw new AccountAlreadyValidatedException("Account already activated");
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

  public void sendMail(User user) {
    String activationLink = baseUrl + "auth/activate?token=" + activationToken;

    String body = "<p>Hello " + user.getFullname() + ",</p>" +
        "<p>Kindly click the link below to activate your account:</p>" +
        "<p><a href=\"" + activationLink + "\">Activate Account</a></p>";

    emailService.sendSimpleMail(user.getEmail(), body,
        "Finance Manager account activation");
  }

}
