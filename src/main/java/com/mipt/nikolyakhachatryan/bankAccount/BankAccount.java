package com.mipt.nikolyakhachatryan.bankAccount;

public class BankAccount {
  private final long id;
  private volatile int balance;
  private static long nextId = 1L;

  public BankAccount(int initialBalance) {
    this.id = nextId++;
    if (initialBalance < 0) {
      throw new IllegalArgumentException("Initial balance cannot be negative");
    }
    this.balance = initialBalance;
  }

  public long getId() {
    return id;
  }

  public synchronized int getBalance() {
    return balance;
  }

  public synchronized void withdraw(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Withdraw amount must be non-negative");
    }
    if (balance < amount) {
      throw new IllegalStateException("Insufficient funds");
    }
    balance -= amount;
  }

  public synchronized void deposit(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Deposit amount must be non-negative");
    }
    balance += amount;
  }

  @Override
  public String toString() {
    return "BankAccount{id=" + id + ", balance=" + balance + '}';
  }
}