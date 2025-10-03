package com.microservice.order_service.service;

import com.microservice.order_service.entity.Order;
import com.microservice.order_service.exception.OrderException;
import com.microservice.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Optional<Order> findById(Long id) {
		return orderRepository.findById(id);
	}

	public List<Order> findByUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	public Order save(Order order) {
		order.setOrderDate(order.getOrderDate() == null ? java.time.LocalDateTime.now() : order.getOrderDate());
		order.setOrderPlaced(true);
		return orderRepository.save(order);
	}

	public void deleteById(Long id) {
		if (!orderRepository.existsById(id)) {
			throw new OrderException("Order not found with id: " + id);
		}
		orderRepository.deleteById(id);
	}
}
