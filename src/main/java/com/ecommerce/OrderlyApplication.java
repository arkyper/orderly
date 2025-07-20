package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Main Spring Boot application class for the E-commerce Order Management System.
// This application provides REST APIs for order placement with inventory management.

@SpringBootApplication
public class OrderlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderlyApplication.class, args);
	}
}