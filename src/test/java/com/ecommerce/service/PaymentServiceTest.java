package com.ecommerce.service;

import com.ecommerce.exception.PaymentFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private Random random;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processPayment_success() {
        when(random.nextDouble()).thenReturn(0.8); // Simulate success chance

        BigDecimal amount = new BigDecimal("100.00");
        String customerEmail = "test@example.com";

        String transactionId = paymentService.processPayment(amount, customerEmail);

        assertNotNull(transactionId);
        assertTrue(transactionId.startsWith("TXN-"));
    }

    @Test
    void processPayment_failure() {
        when(random.nextDouble()).thenReturn(0.1); // Simulate failure chance

        BigDecimal amount = new BigDecimal("100.00");
        String customerEmail = "test@example.com";

        assertThrows(PaymentFailedException.class, () -> paymentService.processPayment(amount, customerEmail));
    }
}