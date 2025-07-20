# Orderly E-commerce Backend

## Overview

This project is a simple e-commerce backend system focusing on order placement with integrated inventory management. The core workflow for placing an order involves:

1.  **Validation:** The system validates that all products in the order request exist.
2.  **Stock Check and Lock:** It checks the available stock for each product and attempts to lock the required quantity to prevent overselling.
3.  **Payment Processing:** A mock payment service is called to simulate payment processing.
4.  **Order Saving:** If payment is successful, the order details are saved to the database.
5.  **Stock Deduction:** Finally, the stock quantities for the ordered products are deducted.

## Technologies Used

*   Java 17
*   Spring Boot 3.2
*   Maven
*   Spring Data JPA (with H2 in-memory database for simplicity)
*   Lombok (for reducing boilerplate code)
*   JUnit 5 and Mockito (for testing)
*   Google Generative AI SDK for Java (for potential future AI features)

## Components

*   **Controller:** Handles incoming HTTP requests and returns responses. It acts as the entry entry point for the API. Responsible for parsing request bodies, validating input, and serializing responses.
*   **Service:** Contains the business logic of the application. It orchestrates interactions between repositories and other services to fulfill requests. Examples include the order placement workflow, inventory updates, and payment processing.
*   **Repository:** Provides an interface for interacting with the database. It abstracts away the details of data access and persistence, allowing the service layer to work with objects.
*   **Models:** Represents the data structures used in the application. These often map directly to database tables and define the structure of the data being processed.

## REST Endpoints

Here are the main REST endpoints provided by the application, along with sample request and response examples in JSON format:

### 1. Place a New Order

`POST /orders`

Places a new order in the system.

**Request Body Example:**

```json
{
  "customerEmail": "test@example.com",
  "shippingAddress": "123 Main St, Anytown CA 91234",
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

**Response Body Example (Success - HTTP 201 Created):**

```json
{
  "orderId": 101,
  "customerEmail": "test@example.com",
  "shippingAddress": "123 Main St, Anytown CA 91234",
  "totalAmount": 75.00,
  "orderItems": [
    {
      "productId": 1,
      "productName": "Product A",
      "quantity": 2,
      "price": 25.00
    },
    {
      "productId": 2,
      "productName": "Product B",
      "quantity": 1,
      "price": 25.00
    }
  ]
}
```

**Response Body Example (Failure - HTTP 400 Bad Request or HTTP 500 Internal Server Error):**

```json
{
  "timestamp": "2023-10-27T10:30:00.123+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Product with ID 99 not found.",
  "path": "/orders"
}
```
(Error response structure may vary based on the specific exception)

### 2. Fetch Order by ID

`GET /orders/{id}`

Retrieves the details of a specific order.

**Path Parameter:**

*   `id`: The ID of the order to fetch.

**Response Body Example (Success - HTTP 200 OK):**

```json
{
  "orderId": 101,
  "customerEmail": "test@example.com",
  "shippingAddress": "123 Main St, Anytown CA 91234",
  "totalAmount": 75.00,
  "orderItems": [
    {
      "productId": 1,
      "productName": "Product A",
      "quantity": 2,
      "price": 25.00
    },
    {
      "productId": 2,
      "productName": "Product B",
      "quantity": 1,
      "price": 25.00
    }
  ]
}
```

**Response Body Example (Order Not Found - HTTP 404 Not Found):**

```json
{
  "timestamp": "2023-10-27T10:35:00.456+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Order with ID 999 not found.",
  "path": "/orders/999"
}
```

### 3. Check Product Stock

`GET /inventory/{productId}`

Retrieves the stock information for a specific product.

**Path Parameter:**

*   `productId`: The ID of the product to check.

**Response Body Example (Success - HTTP 200 OK):**

```json
{
  "productId": 1,
  "productName": "Product A",
  "price": 25.00,
  "stockQuantity": 100
}
```

**Response Body Example (Product Not Found - HTTP 404 Not Found):**

```json
{
  "timestamp": "2023-10-27T10:40:00.789+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product with ID 99 not found.",
  "path": "/inventory/99"
}
```