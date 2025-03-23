package com.example.instant_payment_demo.notification;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    private static final String TOPIC_NAME_SUCCESS = "transaction-success";

    private static final String TOPIC_NAME_FAILURE = "transaction-failure";

    @Bean
    public NewTopic transactionEventsTopic() {
        return TopicBuilder.name(TOPIC_NAME_SUCCESS)
                .partitions(3)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000")
                .build();
    }

    @Bean
    public NewTopic transactionFailuresTopic() {
       return TopicBuilder.name(TOPIC_NAME_FAILURE)
                .partitions(3)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000")
                .build();
    }
}
