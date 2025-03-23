package com.example.instant_payment_demo.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = {"transaction-success", "transaction-failure"}, groupId = "payment_group")
    public void consumeMessage(String message) {
        logger.info("Notification Received: {}", message);
    }
}
