package com.ecommerce.config;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Initializes sample data when the application starts.

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Autowired
    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample products
        if (productRepository.count() == 0) {
            productRepository.save(new Product(null, "Laptop", new BigDecimal("59.99"), 10));
            productRepository.save(new Product(null, "Mouse", new BigDecimal("29.99"), 50));
            productRepository.save(new Product(null, "Keyboard", new BigDecimal("39.99"), 25));

            System.out.println("Sample products initialized:");
            System.out.println("- Product 1: Laptop ($59.99, Stock: 10)");
            System.out.println("- Product 2: Mouse ($29.99, Stock: 50)");
            System.out.println("- Product 3: Keyboard ($39.99, Stock: 25)");
        }
    }
}