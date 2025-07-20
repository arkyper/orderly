package com.ecommerce.service;

import com.ecommerce.exception.OutOfStockException;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

// Service for managing product inventory operations.

@Service
@Transactional
public class InventoryService {

    private final ProductRepository productRepository;
    private final Map<Long, Integer> lockedStock = new HashMap<>();

    @Autowired
    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    
    // Check if sufficient stock is available for a product.
    
    // @param productId the product ID
    // @param quantity required quantity
    // @return true if sufficient stock is available
    
    public boolean isStockAvailable(Long productId, Integer quantity) {
        Product product = getProduct(productId);
        int availableStock = product.getStockQuantity() - lockedStock.getOrDefault(productId, 0);
        return availableStock >= quantity;
    }

    
    // Lock stock for a product to reserve it for an order.

    // @param productId the product ID
    // @param quantity quantity to lock
    // @throws ProductNotFoundException if product doesn't exist
    // @throws OutOfStockException if insufficient stock available
    
    public void lockStock(Long productId, Integer quantity) {
        Product product = getProduct(productId);
        int currentLocked = lockedStock.getOrDefault(productId, 0);
        int availableStock = product.getStockQuantity() - currentLocked;

        if (availableStock < quantity) {
            throw new OutOfStockException(product.getName(), availableStock, quantity);
        }

        lockedStock.put(productId, currentLocked + quantity);
    }

    
    // Deduct stock from inventory after successful order processing.

    // @param productId the product ID
    // @param quantity quantity to deduct
    
    public void deductStock(Long productId, Integer quantity) {
        Product product = getProduct(productId);
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        // Release locked stock
        int currentLocked = lockedStock.getOrDefault(productId, 0);
        int newLocked = Math.max(0, currentLocked - quantity);
        if (newLocked == 0) {
            lockedStock.remove(productId);
        } else {
            lockedStock.put(productId, newLocked);
        }
    }

    
    // Release locked stock (in case of order failure).

    // @param productId the product ID
    // @param quantity quantity to release
    
    public void releaseLock(Long productId, Integer quantity) {
        int currentLocked = lockedStock.getOrDefault(productId, 0);
        int newLocked = Math.max(0, currentLocked - quantity);
        if (newLocked == 0) {
            lockedStock.remove(productId);
        } else {
            lockedStock.put(productId, newLocked);
        }
    }

    
    // Get product by ID.

    // @param productId the product ID
    // @return the product
    // @throws ProductNotFoundException if product doesn't exist
    
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}