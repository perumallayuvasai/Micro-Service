package com.microservice.product_service.service;

import com.microservice.product_service.entity.Product;
import com.microservice.product_service.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	private static final String USER_SERVICE_URL = "http://user-service/users";

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	public Product save(Product product) {
		return productRepository.save(product);
	}

	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}

	// Reduce quantity
	public Product reduceQuantity(Long productId, int quantity, Long userId) {
		String role = getUserRole(userId);
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

		if (product.getQuantity() < quantity) {
			throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
		}

		product.setQuantity(product.getQuantity() - quantity);
		return productRepository.save(product);
	}

	// Helper method to check user role
	private String getUserRole(Long userId) {
		ResponseEntity<Map> response = restTemplate.getForEntity(USER_SERVICE_URL + "/" + userId, Map.class);
		Map<String, Object> user = response.getBody();
		if (user == null || !user.containsKey("role")) {
			throw new IllegalArgumentException("User not found or role not defined");
		}
		return user.get("role").toString();
	}
}