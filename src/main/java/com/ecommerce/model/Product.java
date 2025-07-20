package com.ecommerce.model;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;

// Product entity representing items available in the inventory.

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

}