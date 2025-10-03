package com.microservice.user_service.controller;

import com.microservice.user_service.entity.User;
import com.microservice.user_service.exception.UserException;
import com.microservice.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public List<User> getAllUsers() {
		return userService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		return userService.findById(id).map(ResponseEntity::ok)
				.orElseThrow(() -> new UserException("User not found with id: " + id));
	}

	@PostMapping
	public User createUser(@RequestBody User user) {
		return userService.save(user);
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
		return userService.findById(id).map(user -> {
			user.setUsername(userDetails.getUsername());
			user.setEmail(userDetails.getEmail());
			user.setPassword(userDetails.getPassword());
			user.setRole(userDetails.getRole());
			return ResponseEntity.ok(userService.save(user));
		}).orElseThrow(() -> new UserException("User not found with id: " + id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}