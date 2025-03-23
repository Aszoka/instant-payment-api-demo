package com.example.instant_payment_demo.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PaymentController.class)
@ActiveProfiles(value = "test")
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService transactionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldMakePaymentSuccessfully() throws Exception {
        PaymentRequest request = new PaymentRequest(1L, 2L, new BigDecimal("100.50"));

       when(transactionService.processPayment(any(PaymentRequest.class))).thenReturn( ResponseEntity.ok("Payment successful"));

        mockMvc.perform(post("/api/payments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment successful"));
    }

    @Test
    void shouldReturnBadRequestWhenPaymentFails() throws Exception {
        PaymentRequest request = new PaymentRequest(1L, 2L, new BigDecimal("100.50"));

        when(transactionService.processPayment(any(PaymentRequest.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient balance."));

        mockMvc.perform(post("/api/payments")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(status().reason("Insufficient balance."));
    }
}