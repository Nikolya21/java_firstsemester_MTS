package com.mipt.nikolyakhachatryan.bankAccount2;

import java.util.concurrent.atomic.AtomicLong;

public class BankAccount {
  private final long id;
  private volatile int balance;

  private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

  public BankAccount(int initialBalance) {
    if (initialBalance < 0) {
      throw new IllegalArgumentException("Initial balance cannot be negative");
    }
    this.id = ID_GENERATOR.incrementAndGet();
    this.balance = initialBalance;
  }

  public long getId() {
    return id;
  }

  public int getBalance() {
    return balance;
  }

  void withdraw(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Withdraw amount must be positive");
    }
    if (this.balance < amount) {
      throw new InsufficientFundsException("Insufficient funds: " + this.balance + " < " + amount);
    }
    this.balance -= amount;
  }

  void deposit(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Deposit amount must be positive");
    }
    this.balance += amount;
  }
}
