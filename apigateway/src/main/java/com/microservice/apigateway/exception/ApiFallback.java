package com.microservice.apigateway.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ApiFallback {

	// Gateway routes forward to /fallback when downstream is unavailable
	@GetMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("Fallback: One of the downstream services is currently unavailable. Please try again later.");
	}
}
