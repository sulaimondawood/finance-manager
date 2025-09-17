package com.dawood.finance.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dawood.finance.dtos.ErrorResponse;
import com.dawood.finance.exceptions.category.CategoryNotFoundException;
import com.dawood.finance.exceptions.expense.ExpenseNotFoundException;
import com.dawood.finance.exceptions.income.IncomeNotFoundException;
import com.dawood.finance.exceptions.user.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableExceptionHandler(
      HttpMessageNotReadableException ex) {

    String readableMessage = "Invalid request body. Please check your JSON format or field values.";
    ErrorResponse response = ErrorResponse.builder()

        .message(readableMessage)
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedExceptionHandler(
      HttpRequestMethodNotSupportedException ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message(ex.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundExceptionHandler(
      UserNotFoundException ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message(ex.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(IncomeNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleIncomeNotFoundExceptionHandler(IncomeNotFoundException ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message(ex.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(ExpenseNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleExpenseNotFoundExceptionHandler(ExpenseNotFoundException ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message(ex.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleCategoryNotFoundExceptionHandler(CategoryNotFoundException ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message(ex.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> dataIntegrityExceptionHandler(DataIntegrityViolationException ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message("A data validation error occured, please verify your input and try again")
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> illegallArugmentExceptionHandler(IllegalArgumentException ex) {

    ErrorResponse response = ErrorResponse.builder()

        .message(ex.getMessage())
        .status(HttpStatus.BAD_REQUEST.value())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

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
