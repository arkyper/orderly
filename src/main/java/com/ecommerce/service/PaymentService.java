package com.ecommerce.service;

import com.ecommerce.exception.PaymentFailedException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Random;

// Mock payment service for simulating payment processing.

@Service
public class PaymentService {

    private final Random random = new Random();

    
    // Process payment for the given amount.
    // Randomly fails 20% of the time to simulate real-world scenarios.
    
    // @param amount the payment amount
    // @param customerEmail customer email for payment processing
    // @return payment transaction ID
    // @throws PaymentFailedException if payment fails
    
    public String processPayment(BigDecimal amount, String customerEmail) {
        // Simulate payment processing delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate payment failure (20% chance)
        if (random.nextDouble() < 0.2) {
            throw new PaymentFailedException("Payment processing failed");
        }

        // Generate mock transaction ID
        return "TXN-" + System.currentTimeMillis();
    }
}