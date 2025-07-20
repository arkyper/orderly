# Orderly E-commerce Backend

A simple Spring Boot application for handling e-commerce order placement with inventory management and payment processing.

## Overview

This project is a simple e-commerce backend system focusing on order placement with integrated inventory management. The core workflow for placing an order involves:

1.  **Validation:** The system validates that all products in the order request exist.
2.  **Stock Check and Lock:** It checks the available stock for each product and attempts to lock the required quantity to prevent overselling.
3.  **Payment Processing:** A mock payment service is called to simulate payment processing.
4.  **Order Saving:** If payment is successful, the order details are saved to the database.
5.  **Stock Deduction:** Finally, the stock quantities for the ordered products are deducted.

## Technologies Used

- **Java 17**
- **Spring Boot 3.2.0**
- **Maven** - Build tool
- **H2 Database** - In-memory database
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **Spring Boot Starter Web** - REST API
- **Spring Boot Starter Data JPA** - Data persistence


## Project Structure

```
src/main/java/com/ecommerce/
├── EcommerceApplication.java          # Main application class
├── controller/
│   ├── OrderController.java           # REST endpoints for orders
│   └── InventoryController.java       # REST endpoints for inventory
|   └── GlobalExceptionHandler.java    # Global exception handling
├── service/
│   ├── OrderService.java              # Order business logic
│   ├── InventoryService.java          # Inventory management
│   └── PaymentService.java            # Payment processing
├── repository/
│   ├── OrderRepository.java           # Order data access
│   └── ProductRepository.java         # Product data access
├── model/
│   ├── Order.java                     # Order entity
│   ├── Product.java                   # Product entity
│   └── OrderItem.java                 # Order item entity
├── dto/
│   ├── OrderRequest.java              # Order creation request
│   ├── OrderResponse.java             # Order response
│   └── OrderItemRequest.java          # Order item request
│   └── OrderItemResponse.java         # Order item response
└── exception/
    ├── ProductNotFoundException.java   # Custom exceptions
    ├── OutOfStockException.java
    └── PaymentFailedException.java
```


## Components Description

### Controllers
- **OrderController**: Handles HTTP requests for order operations (create, retrieve)
- **InventoryController**: Provides endpoints to check product stock levels

### Services
- **OrderService**: Core business logic for order processing workflow
- **InventoryService**: Manages product inventory, stock validation and deduction
- **PaymentService**: Mock payment processing with configurable failure scenarios

### Repositories
- **OrderRepository**: JPA repository for order persistence
- **ProductRepository**: JPA repository for product data access

### Models
- **Order**: Order entity with customer info and order items
- **Product**: Product entity with stock information
- **OrderItem**: Individual items within an order


## REST API Endpoints

### Place Order
**POST** `/orders`

**Request Body:**
```json
{
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

**Response (Success - 201 Created):**
```json
{
  "id": 1,
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "totalAmount": 149.97,
  "status": "COMPLETED",
  "orderDate": "2024-01-15T10:30:00",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "quantity": 2,
      "price": 59.99,
      "subtotal": 119.98
    },
    {
      "id": 2,
      "productId": 2,
      "productName": "Mouse",
      "quantity": 1,
      "price": 29.99,
      "subtotal": 29.99
    }
  ]
}
```

**Error Responses:**
```json
// Product not found (404)
{
  "error": "Product not found with id: 999"
}

// Out of stock (400)
{
  "error": "Out of stock for product: Laptop. Available: 5, Requested: 10"
}

// Payment failed (400)
{
  "error": "Payment processing failed"
}
```

### Get Order by ID
**GET** `/orders/{id}`

**Response (200 OK):**
```json
{
  "id": 1,
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "totalAmount": 149.97,
  "status": "COMPLETED",
  "orderDate": "2024-01-15T10:30:00",
  "items": [...]
}
```

### Check Product Stock
**GET** `/inventory/{productId}`

**Response (200 OK):**
```json
{
  "productId": 1,
  "productName": "Laptop",
  "price": 59.99,
  "stockQuantity": 8
}
```

## Running the Application

1. **Prerequisites**: Java 17, Maven
2. **Clone/Download** the project files
3. **Build**: `mvn clean install`
4. **Run**: `mvn spring-boot:run`
5. **Access**: Application runs on `http://localhost:8080`

## Database

- **H2 Console**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (empty)

## Sample Data

The application initializes with sample products:
- Product 1: Laptop ($59.99, Stock: 10)
- Product 2: Mouse ($29.99, Stock: 50)
- Product 3: Keyboard ($39.99, Stock: 25)

## Testing

Run unit tests with: `mvn test`

The test suite covers:
- Order service business logic
- Inventory management
- Payment processing scenarios
- Edge cases (out of stock, payment failures, product not found)

## Order Processing Workflow

1. **Validation**: Check if all products exist
2. **Stock Check**: Verify sufficient inventory
3. **Stock Lock**: Reserve inventory for the order
4. **Payment**: Process payment through mock service
5. **Order Creation**: Save order to database
6. **Stock Deduction**: Confirm inventory reduction
7. **Response**: Return order confirmation

## Error Handling

The application handles various scenarios:
- **Product Not Found**: Returns 404 with descriptive message
- **Out Of Stock**: Returns 400 with current stock info
- **Payment Failure**: Returns 400 with payment error
- **Invalid Input**: Returns 400 with validation errors