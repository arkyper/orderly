package com.ecommerce.repository;

import com.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for Order entity data access.

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}