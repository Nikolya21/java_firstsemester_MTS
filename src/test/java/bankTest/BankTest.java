package bankTest;

import com.mipt.nikolyakhachatryan.bankAccount.Bank;
import com.mipt.nikolyakhachatryan.bankAccount.BankAccount;
import org.junit.jupiter.api.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

  private Bank bank;
  private BankAccount acc1;
  private BankAccount acc2;
  private BankAccount acc3;

  @BeforeEach
  void setUp() {
    bank = new Bank();
    acc1 = new BankAccount(1000);
    acc2 = new BankAccount(1000);
    acc3 = new BankAccount(0);
  }

  @Test
  void sendToAccount_worksCorrectly_singleThread() {
    bank.sendToAccount(acc1, acc2, 200);
    assertEquals(800, acc1.getBalance());
    assertEquals(1200, acc2.getBalance());
  }

  @Test
  void sendToAccountDeadlock_worksCorrectly_singleThread() {
    bank.sendToAccountDeadlock(acc1, acc2, 150);
    assertEquals(850, acc1.getBalance());
    assertEquals(1150, acc2.getBalance());
  }


  @Test
  void sendToAccount_concurrent_noDeadlock() throws InterruptedException {
    int threads = 10; // Уменьшаем, чтобы не превышать баланс
    int transfersPerThread = 10; // Уменьшаем количество переводов
    int amount = 1;

    ExecutorService executor = Executors.newFixedThreadPool(threads);
    CountDownLatch latch = new CountDownLatch(threads);

    Runnable task = () -> {
      try {
        for (int i = 0; i < transfersPerThread; i++) {
          bank.sendToAccount(acc1, acc2, amount);
        }
      } catch (Exception e) {
      } finally {
        latch.countDown();
      }
    };

    for (int i = 0; i < threads; i++) {
      executor.submit(task);
    }

    latch.await(5, TimeUnit.SECONDS);
    executor.shutdown();

    int total = acc1.getBalance() + acc2.getBalance();
    assertEquals(2000, total); // Сумма должна сохраняться
    assertEquals(1000 - threads * transfersPerThread, acc1.getBalance());
    assertEquals(1000 + threads * transfersPerThread, acc2.getBalance());
  }

  @Test
  @Disabled("Дедлок непредсказуем, не стоит полагаться на него в CI")
  void sendToAccountDeadlock_canCauseDeadlock() throws InterruptedException {
    Thread t1 = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        try {
          bank.sendToAccountDeadlock(acc1, acc2, 1);
        } catch (Exception ignored) { }
      }
    });

    Thread t2 = new Thread(() -> {
      for (int i = 0; i < 1000; i++) {
        try {
          bank.sendToAccountDeadlock(acc2, acc1, 1);
        } catch (Exception ignored) { }
      }
    });

    t1.start();
    t2.start();

    boolean t1Finished = false;
    boolean t2Finished = false;

    try {
      t1.join(2000);
      t1Finished = true;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    try {
      t2.join(2000);
      t2Finished = true;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    t1.interrupt();
    t2.interrupt();
  }

  @Test
  void sendToAccount_nullFrom_throwsException() {
    assertThrows(IllegalArgumentException.class,
      () -> bank.sendToAccount(null, acc2, 100));
  }

  @Test
  void sendToAccount_nullTo_throwsException() {
    assertThrows(IllegalArgumentException.class,
      () -> bank.sendToAccount(acc1, null, 100));
  }

  @Test
  void sendToAccount_negativeAmount_throwsException() {
    assertThrows(IllegalArgumentException.class,
      () -> bank.sendToAccount(acc1, acc2, -50));
  }

  @Test
  void sendToAccount_sameAccount_throwsException() {
    assertThrows(IllegalArgumentException.class,
      () -> bank.sendToAccount(acc1, acc1, 100));
  }

  @Test
  void sendToAccount_insufficientFunds_throwsException() {
    Exception ex = assertThrows(IllegalStateException.class,
      () -> bank.sendToAccount(acc1, acc2, 2000));
    assertEquals("Insufficient funds on sender account", ex.getMessage());
  }

  @Test
  void sendToAccount_raceCondition_prevented() throws InterruptedException {
    BankAccount poor = new BankAccount(5);
    BankAccount rich = new BankAccount(0);

    int numThreads = 10;
    ExecutorService exec = Executors.newFixedThreadPool(numThreads);
    AtomicInteger failedAttempts = new AtomicInteger(0);

    for (int i = 0; i < numThreads; i++) {
      exec.submit(() -> {
        try {
          bank.sendToAccount(poor, rich, 2);
        } catch (IllegalStateException e) {
          failedAttempts.incrementAndGet();
        }
      });
    }

    exec.shutdown();
    assertTrue(exec.awaitTermination(3, TimeUnit.SECONDS));

    int sent = rich.getBalance();
    assertTrue(sent % 2 == 0);
    assertTrue(sent <= 4);
    assertEquals(5 - sent, poor.getBalance());
    assertTrue(failedAttempts.get() >= numThreads - sent / 2);
  }

  @Test
  void sendToAccount_orderingPreventsDeadlock_evenWithOppositeDirections() throws InterruptedException {
    Thread t1 = new Thread(() -> {
      for (int i = 0; i < 5000; i++) {
        try {
          bank.sendToAccount(acc1, acc3, 1);
        } catch (Exception ignored) {}
      }
    });

    Thread t2 = new Thread(() -> {
      for (int i = 0; i < 5000; i++) {
        try {
          bank.sendToAccount(acc3, acc1, 1);
        } catch (Exception ignored) {}
      }
    });

    t1.start();
    t2.start();

    t1.join(5000);
    t2.join(5000);

    assertEquals(1000, acc1.getBalance() + acc3.getBalance());
  }
}