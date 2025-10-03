package com.microservice.order_service.controller;

import com.microservice.order_service.entity.Order;
import com.microservice.order_service.exception.OrderException;
import com.microservice.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public List<Order> getAllOrders() {
		return orderService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
		return orderService.findById(id).map(ResponseEntity::ok)
				.orElseThrow(() -> new OrderException("Order not found with id: " + id));
	}

	@GetMapping("/user/{userId}")
	public List<Order> getOrdersByUser(@PathVariable Long userId) {
		return orderService.findByUserId(userId);
	}

	@PostMapping
	public Order createOrder(@RequestBody Order order) {
		return orderService.save(order);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
		return orderService.findById(id).map(order -> {
			order.setUserId(orderDetails.getUserId());
			order.setProductId(orderDetails.getProductId());
			order.setQuantity(orderDetails.getQuantity());
			order.setTotalPrice(orderDetails.getTotalPrice());
			order.setOrderDate(orderDetails.getOrderDate());
			order.setOrderPlaced(orderDetails.isOrderPlaced());
			return ResponseEntity.ok(orderService.save(order));
		}).orElseThrow(() -> new OrderException("Order not found with id: " + id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
		orderService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
