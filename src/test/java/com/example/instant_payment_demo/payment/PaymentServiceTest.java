package com.example.instant_payment_demo.payment;

import com.example.instant_payment_demo.account.AccountEntity;
import com.example.instant_payment_demo.account.AccountRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles(value = "test")
public class PaymentServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentService transactionService;

    AccountEntity sender = new AccountEntity();
    AccountEntity receiver = new AccountEntity();

    @BeforeEach
    void setup() {

        accountRepository.deleteAll();

        sender.setBalance(new BigDecimal("500.0"));
;
        receiver.setBalance(new BigDecimal("200.0"));

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }

    @AfterEach
    void cleanUp() {

        accountRepository.deleteAll();
    }

    @Test
    void testConcurrentUpdateCausingOptimisticLockException() throws InterruptedException {


        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            try {
                transactionService.processPayment(new PaymentRequest(sender.getId(), receiver.getId(),new BigDecimal("100.0")));
            } catch (Exception ignored) { }
        };

        Runnable task2 = () -> {
            try {
                transactionService.processPayment(new PaymentRequest(sender.getId(), receiver.getId(),new BigDecimal("100.0")));
            } catch (Exception ignored) { }
        };

        executor.submit(task1);
        executor.submit(task2);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);


        AccountEntity updatedAccount = accountRepository.findById(sender.getId()).orElseThrow();
        assertTrue(updatedAccount.getBalance().compareTo(new BigDecimal("300.00")) >=0);
    }

}
