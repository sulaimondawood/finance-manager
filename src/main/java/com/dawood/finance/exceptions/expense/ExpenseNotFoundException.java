package com.dawood.finance.exceptions.expense;

public class ExpenseNotFoundException extends RuntimeException {
  public ExpenseNotFoundException(String message) {
    super(message);
  }

  public ExpenseNotFoundException() {
    super("Expense not found");
  }
}
