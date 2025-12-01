package com.mipt.nikolyakhachatryan.bankAccount2;

public class InsufficientFundsException extends RuntimeException {
  public InsufficientFundsException(String message) {
    super(message);
  }
}
