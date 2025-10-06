package com.microservice.order_service.controller;

import com.microservice.order_service.entity.Order;
import com.microservice.order_service.exception.OrderException;
import com.microservice.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTests {

	@Mock
	private OrderService orderService;

	@InjectMocks
	private OrderController orderController;

	private Order order;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		order = new Order();
		order.setId(1L);
		order.setUserId(101L);
		order.setProductId(201L);
		order.setQuantity(2);
		order.setTotalPrice(500);
		order.setOrderDate(LocalDateTime.now());
		order.setOrderPlaced(true);
	}

	@Test
	void testGetAllOrders() {
		when(orderService.findAll()).thenReturn(Arrays.asList(order));
		assertEquals(1, orderController.getAllOrders().size());
		verify(orderService, times(1)).findAll();
	}

	@Test
	void testGetOrderByIdFound() {
		when(orderService.findById(1L)).thenReturn(Optional.of(order));
		ResponseEntity<Order> response = orderController.getOrderById(1L);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals(101L, response.getBody().getUserId());
	}

	@Test
	void testGetOrderByIdNotFound() {
		when(orderService.findById(1L)).thenReturn(Optional.empty());
		OrderException ex = assertThrows(OrderException.class, () -> orderController.getOrderById(1L));
		assertEquals("Order not found with id: 1", ex.getMessage());
	}

//	@Test
//	void testCreateOrder() {
//		when(orderService.save(order)).thenReturn(order);
//		Order created = orderController.createOrder(order);
//		assertEquals(500, created.getTotalPrice());
//		assertTrue(created.isOrderPlaced());
//	}

	@Test
	void testDeleteOrder() {
		doNothing().when(orderService).deleteById(1L);
		ResponseEntity<Void> response = orderController.deleteOrder(1L);
		assertEquals(204, response.getStatusCodeValue());
		verify(orderService, times(1)).deleteById(1L);
	}
}
