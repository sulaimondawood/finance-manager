package com.dawood.finance.exceptions.user;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException() {
    super("User does not exists");
  }

}
