package com.mipt.nikolyakhachatryan.bankAccount2;

public class Bank {
  public static void sendToAccount(BankAccount from, BankAccount to, int amount) {
    validateAccounts(from, to);
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }

    if (from.getId() < to.getId()) {
      synchronized (from) {
        synchronized (to) {
          performTransfer(from, to, amount);
        }
      }
    } else if (from.getId() > to.getId()) {
      synchronized (to) {
        synchronized (from) {
          performTransfer(from, to, amount);
        }
      }
    } else {
      throw new IllegalStateException("Same account used as from and to with identical id");
    }
  }

  public static void sendToAccountDeadlock(BankAccount from, BankAccount to, int amount) {
    validateAccounts(from, to);
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }

    synchronized (from) {
      synchronized (to) {
        performTransfer(from, to, amount);
      }
    }
  }

  private static void validateAccounts(BankAccount from, BankAccount to) {
    if (from == null) {
      throw new NullPointerException("from account is null");
    }
    if (to == null) {
      throw new NullPointerException("to account is null");
    }
    if (from == to) {
      throw new IllegalArgumentException("Cannot transfer to the same account");
    }
  }

  private static void performTransfer(BankAccount from, BankAccount to, int amount) {
    from.withdraw(amount);
    to.deposit(amount);
  }
}
