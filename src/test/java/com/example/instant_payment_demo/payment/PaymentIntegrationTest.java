package com.example.instant_payment_demo.payment;

import com.example.instant_payment_demo.InstantPaymentDemoApplication;
import com.example.instant_payment_demo.account.AccountEntity;
import com.example.instant_payment_demo.account.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@EmbeddedKafka(partitions = 1, topics = {"transaction_success", "transaction_failure"})
@SpringBootTest(classes = {InstantPaymentDemoApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
public class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    AccountEntity sender = new AccountEntity();
    AccountEntity receiver = new AccountEntity();

    @BeforeEach
    void setup() {

        accountRepository.deleteAll();

        sender.setBalance(new BigDecimal("500.00"));


        receiver.setBalance(new BigDecimal("500.00"));

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }

    @AfterEach
    void cleanUp() {

        accountRepository.deleteAll();
    }
    @Test
    void shouldProcessPaymentSuccessfully() throws Exception {
        PaymentRequest request = new PaymentRequest(sender.getId(), receiver.getId(), new BigDecimal("100.00"));

        mockMvc.perform(post("/api/payments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment successful"));
    }

    @Test
    void shouldFailIfInsufficientBalance() throws Exception {

        PaymentRequest request = new PaymentRequest(sender.getId(), receiver.getId(), new BigDecimal("600.00"));

        mockMvc.perform(post("/api/payments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(status().reason("Insufficient balance."));
    }
}
