package com.microservice.product_service.service;

import com.microservice.product_service.entity.Product;
import com.microservice.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

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
	void testFindAll() {
		when(productRepository.findAll()).thenReturn(Arrays.asList(product1));
		List<Product> products = productService.findAll();
		assertEquals(1, products.size());
		verify(productRepository, times(1)).findAll();
	}

	@Test
	void testFindById() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
		Optional<Product> product = productService.findById(1L);
		assertTrue(product.isPresent());
		assertEquals("Laptop", product.get().getName());
	}

	@Test
	void testSave() {
		when(productRepository.save(product1)).thenReturn(product1);
		Product saved = productService.save(product1);
		assertEquals("Laptop", saved.getName());
		verify(productRepository, times(1)).save(product1);
	}

	@Test
	void testDeleteById() {
		doNothing().when(productRepository).deleteById(1L);
		productService.deleteById(1L);
		verify(productRepository, times(1)).deleteById(1L);
	}
}
