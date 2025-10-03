package com.microservice.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiGatewayApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	WebTestClient webClient;

	@Test
	void contextLoads() {
	}

	@Test
	void adminEndpoint_requiresBasicAuth_and_allowsAdmin() {
		webClient.get().uri("http://localhost:" + port + "/admin/test")
				.headers(headers -> headers.setBasicAuth("admin", "adminpass")).exchange().expectStatus().isOk()
				.expectBody(String.class).isEqualTo("Admin access granted");
	}

	@Test
	void customerEndpoint_requiresBasicAuth_and_allowsCustomer() {
		webClient.get().uri("http://localhost:" + port + "/customer/test")
				.headers(headers -> headers.setBasicAuth("customer", "custpass")).exchange().expectStatus().isOk()
				.expectBody(String.class).isEqualTo("Customer access granted");
	}
}
