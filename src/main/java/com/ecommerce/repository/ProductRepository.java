package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for Product entity data access.

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}