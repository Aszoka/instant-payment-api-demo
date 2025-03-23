package com.example.instant_payment_demo.transaction;

import com.example.instant_payment_demo.account.AccountEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "TRANSACTION")
public class TransactionEntity {


    @Id
    private UUID transactionId = UUID.randomUUID();

    @ManyToOne
    private AccountEntity sender;

    @ManyToOne
    private AccountEntity receiver;

    @Column
    @Digits(integer = 13, fraction = 2)
    private BigDecimal amount;

    public TransactionEntity() {
    }

    public TransactionEntity(AccountEntity sender, AccountEntity receiver, BigDecimal amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public AccountEntity getSender() {
        return sender;
    }

    public void setSender(AccountEntity sender) {
        this.sender = sender;
    }

    public AccountEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(AccountEntity receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
