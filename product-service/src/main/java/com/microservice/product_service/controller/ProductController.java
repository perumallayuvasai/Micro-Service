package com.microservice.product_service.controller;

import com.microservice.product_service.entity.Product;
import com.microservice.product_service.exception.ProductException;
import com.microservice.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public List<Product> getAllProducts() {
		return productService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		return productService.findById(id).map(ResponseEntity::ok)
				.orElseThrow(() -> new ProductException("Product not found with id: " + id));
	}

	@PostMapping
	public Product createProduct(@RequestBody Product product) {
		return productService.save(product);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
		return productService.findById(id).map(product -> {
			product.setName(productDetails.getName());
			product.setPrice(productDetails.getPrice());
			product.setQuantity(productDetails.getQuantity());
			return ResponseEntity.ok(productService.save(product));
		}).orElseThrow(() -> new ProductException("Product not found with id: " + id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	// Reduce quantity (customer placing an order)
	@PutMapping("/{productId}/reduceQuantity")
	public ResponseEntity<?> reduceQuantity(@PathVariable Long productId, @RequestParam int quantity,
			@RequestParam Long userId) {
		try {
			Product updatedProduct = productService.reduceQuantity(productId, quantity, userId);
			return ResponseEntity.ok(updatedProduct);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (SecurityException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error while reducing quantity: " + e.getMessage());
		}
	}

}