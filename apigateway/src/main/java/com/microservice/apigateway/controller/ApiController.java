package com.microservice.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	@GetMapping("/admin/test")
	public String adminTest() {
		return "Admin access granted";
	}

	@GetMapping("/customer/test")
	public String customerTest() {
		return "Customer access granted";
	}
}
