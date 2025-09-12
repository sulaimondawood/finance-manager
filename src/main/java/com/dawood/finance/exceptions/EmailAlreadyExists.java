package com.dawood.finance.exceptions;

public class EmailAlreadyExists extends RuntimeException {

  public EmailAlreadyExists() {
    super("Email already exists");
  }

  public EmailAlreadyExists(String message) {
    super(message);
  }

}
