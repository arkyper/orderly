package com.ecommerce.service;

import com.ecommerce.dto.OrderItemRequest;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.exception.OrderNotFoundException;
import com.ecommerce.exception.OutOfStockException;
import com.ecommerce.exception.PaymentFailedException;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryService inventoryService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderService orderService;

    private Product product1;
    private Product product2;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "Laptop", new BigDecimal("59.99"), 10);
        product2 = new Product(2L, "Mouse", new BigDecimal("29.99"), 50);

        OrderItemRequest item1 = new OrderItemRequest(1L, 2);
        OrderItemRequest item2 = new OrderItemRequest(2L, 1);

        orderRequest = new OrderRequest("John Doe", "john.doe@example.com", Arrays.asList(item1, item2));
    }

    @Test
    void testCreateOrder_Success() {
        // Mocking dependencies
        when(inventoryService.getProduct(1L)).thenReturn(product1);
        when(inventoryService.getProduct(2L)).thenReturn(product2);
        doNothing().when(inventoryService).lockStock(anyLong(), anyInt());
        doNothing().when(paymentService).processPayment(any(), any());
        when(orderRepository.save(any())).thenAnswer(invocation -> {
            // Simulate saving and returning an order with an ID
            com.ecommerce.model.Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });
        doNothing().when(inventoryService).deductStock(anyLong(), anyInt());

        // Call the service method
        com.ecommerce.dto.OrderResponse orderResponse = orderService.createOrder(orderRequest);

        // Assertions
        assertNotNull(orderResponse);
        assertEquals(1L, orderResponse.getId());
        assertEquals(com.ecommerce.model.Order.OrderStatus.COMPLETED, orderResponse.getStatus());
        assertEquals(2, orderResponse.getItems().size());

        // Verify interactions with dependencies
        verify(inventoryService, times(1)).getProduct(1L);
        verify(inventoryService, times(1)).getProduct(2L);
        verify(inventoryService, times(1)).lockStock(1L, 2);
        verify(inventoryService, times(1)).lockStock(2L, 1);
        verify(paymentService, times(1)).processPayment(any(), any());
        verify(orderRepository, times(1)).save(any());
        verify(inventoryService, times(1)).deductStock(1L, 2);
        verify(inventoryService, times(1)).deductStock(2L, 1);
    }

    @Test
    void testCreateOrder_OutOfStock() {
        // Mocking dependencies for out of stock scenario
        when(inventoryService.getProduct(1L)).thenReturn(product1);
        when(inventoryService.getProduct(2L)).thenReturn(product2);
        doThrow(new OutOfStockException("Laptop", 5, 10))
                .when(inventoryService).lockStock(1L, 2);

        // Call the service method and assert that OutOfStockException is thrown
        assertThrows(OutOfStockException.class, () -> orderService.createOrder(orderRequest));

        // Verify that releaseLock is called for the item that was attempted to be locked
        verify(inventoryService, times(1)).releaseLock(1L, 2);
        // Verify that other methods were not called after the exception
        verify(paymentService, times(0)).processPayment(any(), any());
        verify(orderRepository, times(0)).save(any());
        verify(inventoryService, times(0)).deductStock(anyLong(), anyInt());
    }

    @Test
    void testCreateOrder_PaymentFailed() {
        // Mocking dependencies for payment failed scenario
        when(inventoryService.getProduct(1L)).thenReturn(product1);
        when(inventoryService.getProduct(2L)).thenReturn(product2);
        doNothing().when(inventoryService).lockStock(anyLong(), anyInt());
        doThrow(new PaymentFailedException("Payment failed"))
                .when(paymentService).processPayment(any(), any());

        // Call the service method and assert that PaymentFailedException is thrown
        assertThrows(PaymentFailedException.class, () -> orderService.createOrder(orderRequest));

        // Verify that releaseLock is called for all items when payment fails
        verify(inventoryService, times(1)).releaseLock(1L, 2);
        verify(inventoryService, times(1)).releaseLock(2L, 1);
        // Verify that other methods were not called after the exception
        verify(orderRepository, times(0)).save(any());
        verify(inventoryService, times(0)).deductStock(anyLong(), anyInt());
    }

     @Test
    void testCreateOrder_ProductNotFound() {
        // Mocking dependencies for product not found scenario
        when(inventoryService.getProduct(1L)).thenReturn(product1);
        when(inventoryService.getProduct(2L)).thenThrow(new ProductNotFoundException(2L));

        // Call the service method and assert that ProductNotFoundException is thrown
        assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(orderRequest));

        // Verify that releaseLock is called for the product that was found and locked before the exception
        verify(inventoryService, times(1)).lockStock(1L, 2);
        verify(inventoryService, times(1)).releaseLock(1L, 2);

        // Verify that other methods were not called after the exception
        verify(paymentService, times(0)).processPayment(any(), any());
        verify(orderRepository, times(0)).save(any());
        verify(inventoryService, times(0)).deductStock(anyLong(), anyInt());
    }

    @Test
    void testGetOrder_Success() {
        // Mocking repository response
        com.ecommerce.model.Order mockOrder = new com.ecommerce.model.Order();
        mockOrder.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        // Call the service method
        com.ecommerce.dto.OrderResponse orderResponse = orderService.getOrder(1L);

        // Assertions
        assertNotNull(orderResponse);
        assertEquals(1L, orderResponse.getId());

        // Verify repository interaction
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrder_NotFound() {
        // Mocking repository response for not found scenario
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the service method and assert that OrderNotFoundException is thrown
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(2L));

        // Verify repository interaction
        verify(orderRepository, times(1)).findById(2L);
    }
}
