package com.dawood.finance.exceptions;

public class InvalidActivationToken extends RuntimeException {
  public InvalidActivationToken() {
    super("Invalid activation token");
  }

}
