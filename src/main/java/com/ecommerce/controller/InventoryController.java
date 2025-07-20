package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// REST controller for inventory management operations.

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    
    // Get product information including current stock.
    
    // @param productId the product ID
    // @return product information
    
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductStock(@PathVariable Long productId) {
        Product product = inventoryService.getProduct(productId);
        ProductResponse response = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
        );
        return ResponseEntity.ok(response);
    }

    
    // DTO for product response.
    
    public static class ProductResponse {
        private Long productId;
        private String productName;
        private java.math.BigDecimal price;
        private Integer stockQuantity;

        public ProductResponse(Long productId, String productName,
                               java.math.BigDecimal price, Integer stockQuantity) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.stockQuantity = stockQuantity;
        }

        // Getters
        public Long getProductId() { return productId; }
        public String getProductName() { return productName; }
        public java.math.BigDecimal getPrice() { return price; }
        public Integer getStockQuantity() { return stockQuantity; }
    }
}
