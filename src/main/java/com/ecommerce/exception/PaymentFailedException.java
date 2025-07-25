package com.ecommerce.exception;

// Exception thrown when payment processing fails.

public class PaymentFailedException extends RuntimeException {

    public PaymentFailedException(String message) {
        super(message);
    }

    public PaymentFailedException() {
        super("Payment processing failed");
    }
}