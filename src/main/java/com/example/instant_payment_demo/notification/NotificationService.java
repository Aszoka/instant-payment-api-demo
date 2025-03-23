package com.example.instant_payment_demo.notification;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NotificationService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void notifyTransactionSuccess(Long senderId, Long receiverId, BigDecimal amount) {
        String message = "Transaction successful: Sender ID " + senderId + " sent " + amount + " to Receiver ID " + receiverId;
        kafkaTemplate.send("transaction-success", message);
    }

    public void notifyTransactionFailure(Long senderId, Long receiverId, BigDecimal amount, String reason) {
        String message = "Transaction failed: Sender ID " + senderId + " could not send " + amount + " to Receiver ID " + receiverId + ". Reason: " + reason;
        kafkaTemplate.send("transaction-failure", message);
    }

    public void notifyInsufficientBalance( String reason) {
        String message = "Transaction failed."+  " Reason: " + reason;
        kafkaTemplate.send("transaction-failure", message);
    }
}
