package com.mipt.nikolyakhachatryan.bankAccount;

public class Bank {
  public void sendToAccountDeadlock(BankAccount from, BankAccount to, int amount) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Accounts must not be null");
    }
    if (amount < 0) {
      throw new IllegalArgumentException("Amount must be non-negative");
    }
    if (from == to) {
      throw new IllegalArgumentException("Cannot transfer to self");
    }

    synchronized (from) {
      synchronized (to) {
        if (from.getBalance() < amount) {
          throw new IllegalStateException("Insufficient funds on sender account");
        }
        from.withdraw(amount);
        to.deposit(amount);
      }
    }
  }

  public void sendToAccount(BankAccount from, BankAccount to, int amount) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Accounts must not be null");
    }
    if (amount < 0) {
      throw new IllegalArgumentException("Amount must be non-negative");
    }
    if (from == to) {
      throw new IllegalArgumentException("Cannot transfer to self");
    }
    BankAccount first = from.getId() < to.getId() ? from : to;
    BankAccount second = from.getId() < to.getId() ? to : from;
    synchronized (first) {
      synchronized (second) {
        if (from.getBalance() < amount) {
          throw new IllegalStateException("Insufficient funds on sender account");
        }
        from.withdraw(amount);
        to.deposit(amount);
      }
    }
  }
}
