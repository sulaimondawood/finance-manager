package com.dawood.finance.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.finance.dtos.ApiResponse;
import com.dawood.finance.dtos.auth.LoginRequest;
import com.dawood.finance.dtos.auth.LoginResponse;
import com.dawood.finance.dtos.auth.RegisterRequestDTO;
import com.dawood.finance.dtos.auth.RegisterResponseDTO;
import com.dawood.finance.services.auth.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("register")
  public ResponseEntity<ApiResponse<RegisterResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
    RegisterResponseDTO registerResponseDTO = authService.register(requestDTO);

    return new ResponseEntity<>(ApiResponse.success("Activation link sent to your email", registerResponseDTO),
        HttpStatus.CREATED);
  }

  @PostMapping("login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {

    LoginResponse response = authService.login(request);

    return ResponseEntity.ok()
        .body(ApiResponse.success(response.getMessage(), response));
  }

  @GetMapping("activate")
  public ResponseEntity<ApiResponse<String>> activateAccount(@RequestParam(name = "token") String token,
      HttpServletResponse response) throws IOException {

    String tokenResponse = authService.activateAccount(token);

    response.sendRedirect("http://localhost:3000/login?activated=true");

    return new ResponseEntity<>(
        ApiResponse.success("Account activated successfully", tokenResponse), HttpStatus.OK);
  }

}
