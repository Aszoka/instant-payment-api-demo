package com.example.instant_payment_demo.payment;

import com.example.instant_payment_demo.account.AccountEntity;
import com.example.instant_payment_demo.account.AccountRepository;
import com.example.instant_payment_demo.notification.NotificationService;

import com.example.instant_payment_demo.transaction.TransactionEntity;
import com.example.instant_payment_demo.transaction.TransactionRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@Transactional
public class PaymentService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final NotificationService notificationService;

    public PaymentService(AccountRepository accountRepository, TransactionRepository transactionRepository, NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
    }


    public ResponseEntity<String> processPayment(PaymentRequest request) {

        AccountEntity sender = accountRepository.findById(request.senderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        validateSenderBalance(sender, request.amount());

        AccountEntity receiver = accountRepository.findById(request.receiverId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));


        try {
            updateBalance(sender, request.amount(), false);
            updateBalance(receiver, request.amount(), true);
            TransactionEntity transaction = new TransactionEntity(sender, receiver, request.amount());
            transactionRepository.save(transaction);

            notificationService.notifyTransactionSuccess(request.senderId(),request.receiverId(),  request.amount());
        } catch (OptimisticLockException e) {
            notificationService.notifyTransactionFailure(request.senderId(),request.receiverId(),  request.amount(),"Transaction failed due to concurrency issues.");
        }

        return ResponseEntity.ok("Payment successful");
    }

    public void validateSenderBalance(AccountEntity sender, BigDecimal amount) {

        if (sender.getBalance().compareTo(amount) <0) {
            notificationService.notifyInsufficientBalance("Insufficient balance.");

            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient balance.");
        }
    }

    public void updateBalance(AccountEntity account, BigDecimal amount, boolean isAddition) throws OptimisticLockException{
        BigDecimal balance = isAddition ? account.getBalance().add(amount) : account.getBalance().subtract(amount);
        account.setBalance(balance);

        accountRepository.save(account);
    }

}
