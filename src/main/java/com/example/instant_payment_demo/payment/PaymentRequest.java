package com.example.instant_payment_demo.payment;

import java.math.BigDecimal;

public record PaymentRequest(
        Long senderId,
        Long receiverId,
        BigDecimal amount
) {
    public PaymentRequest(Long senderId, Long receiverId, BigDecimal amount) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }
}
