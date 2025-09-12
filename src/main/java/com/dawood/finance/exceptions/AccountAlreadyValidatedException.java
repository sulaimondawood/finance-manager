package com.dawood.finance.exceptions;

public class AccountAlreadyValidatedException extends RuntimeException {

  public AccountAlreadyValidatedException(String message) {
    super(message);
  }

}
