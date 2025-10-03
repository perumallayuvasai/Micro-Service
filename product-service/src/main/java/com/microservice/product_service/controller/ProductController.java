package com.microservice.product_service.controller;

import com.microservice.product_service.entity.Product;
import com.microservice.product_service.exception.ProductException;
import com.microservice.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
}