package com.dawood.finance.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dawood.finance.dtos.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> methodArgumentNotValidsxceptionHandler(
      MethodArgumentNotValidException ex) {

    Map<String, String> response = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(err -> {
      response.put(err.getField(), err.getDefaultMessage());
    });

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> badCredentialsxceptionHandler(BadCredentialsException ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message(ex.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(InvalidCredentials.class)
  public ResponseEntity<ErrorResponse> authenticationExceptionHandler(InvalidCredentials ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message(ex.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(EmailAlreadyExists.class)
  public ResponseEntity<ErrorResponse> emailAlreadyExistsHandler(EmailAlreadyExists ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message(ex.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);

  }

  @ExceptionHandler(AccountAlreadyValidatedException.class)
  public ResponseEntity<ErrorResponse> emailAlreadyExistsHandler(AccountAlreadyValidatedException ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message(ex.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);

  }

}
