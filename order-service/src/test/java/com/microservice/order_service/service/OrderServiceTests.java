package com.microservice.order_service.service;

import com.microservice.order_service.entity.Order;
import com.microservice.order_service.exception.OrderException;
import com.microservice.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTests {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

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
	void testFindAll() {
		when(orderRepository.findAll()).thenReturn(Arrays.asList(order));
		List<Order> orders = orderService.findAll();
		assertEquals(1, orders.size());
		verify(orderRepository, times(1)).findAll();
	}

	@Test
	void testFindById() {
		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		Optional<Order> found = orderService.findById(1L);
		assertTrue(found.isPresent());
		assertEquals(101L, found.get().getUserId());
	}

	@Test
	void testSave() {
		when(orderRepository.save(order)).thenReturn(order);
		Order saved = orderService.save(order);
		assertEquals(500, saved.getTotalPrice());
		assertTrue(saved.isOrderPlaced());
	}

	@Test
	void testDeleteByIdThrowsException() {
		when(orderRepository.existsById(1L)).thenReturn(false);
		OrderException ex = assertThrows(OrderException.class, () -> orderService.deleteById(1L));
		assertEquals("Order not found with id: 1", ex.getMessage());
	}

	@Test
	void testDeleteByIdSuccess() {
		when(orderRepository.existsById(1L)).thenReturn(true);
		doNothing().when(orderRepository).deleteById(1L);
		orderService.deleteById(1L);
		verify(orderRepository, times(1)).deleteById(1L);
	}
}
