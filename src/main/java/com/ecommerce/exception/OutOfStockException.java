package com.ecommerce.exception;


// Exception thrown when there's insufficient stock for a product.

public class OutOfStockException extends RuntimeException {

    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(String productName, int available, int requested) {
        super(String.format("Insufficient stock for product: %s. Available: %d, Requested: %d",
                productName, available, requested));
    }
}