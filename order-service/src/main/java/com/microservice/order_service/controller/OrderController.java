package com.microservice.order_service.controller;

import com.microservice.order_service.entity.Order;
import com.microservice.order_service.exception.OrderException;
import com.microservice.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private RestTemplate restTemplate;

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

//	@PostMapping
//	public Order createOrder(@RequestBody Order order) {
//		return orderService.save(order);
//	}

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

	// Create an order
	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody Order orderRequest) {
		try {
			// Call Product Service to reduce quantity
			Long productId = orderRequest.getProductId(); // productId field in Order entity
			int orderQuantity = orderRequest.getQuantity();
			Long userId = orderRequest.getUserId();
			restTemplate.put("http://product-service/products/{id}/reduceQuantity?quantity={qty}&userId={userId}", null,
					productId, orderQuantity, userId);

			// Save the order
			Order savedOrder = orderService.createOrder(orderRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
		} catch (HttpClientErrorException e) {
			// Handle product-service errors (like insufficient stock)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
}
