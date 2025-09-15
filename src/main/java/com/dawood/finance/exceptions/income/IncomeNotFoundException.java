package com.dawood.finance.exceptions.income;

public class IncomeNotFoundException extends RuntimeException {
  public IncomeNotFoundException(String message) {
    super(message);
  }

  public IncomeNotFoundException() {
    super("Income not found");
  }
}
