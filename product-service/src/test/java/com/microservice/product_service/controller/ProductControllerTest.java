package com.microservice.product_service.controller;

import com.microservice.product_service.entity.Product;
import com.microservice.product_service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

	@Mock
	private ProductService productService;

	@InjectMocks
	private ProductController productController;

	private Product product1;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		product1 = new Product();
		product1.setId(1L);
		product1.setName("Laptop");
		product1.setCategory("Electronics");
		product1.setPrice(50000);
		product1.setQuantity(5);
	}

	@Test
	void testGetAllProducts() {
		when(productService.findAll()).thenReturn(Arrays.asList(product1));
		assertEquals(1, productController.getAllProducts().size());
		verify(productService, times(1)).findAll();
	}

	@Test
	void testGetProductById() {
		when(productService.findById(1L)).thenReturn(Optional.of(product1));
		ResponseEntity<Product> response = productController.getProductById(1L);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Laptop", response.getBody().getName());
	}

	@Test
	void testCreateProduct() {
		when(productService.save(product1)).thenReturn(product1);
		Product created = productController.createProduct(product1);
		assertEquals("Laptop", created.getName());
		verify(productService, times(1)).save(product1);
	}

	@Test
	void testDeleteProduct() {
		doNothing().when(productService).deleteById(1L);
		ResponseEntity<Void> response = productController.deleteProduct(1L);
		assertEquals(204, response.getStatusCodeValue());
		verify(productService, times(1)).deleteById(1L);
	}
}
