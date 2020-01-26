package team.gif.friendscheduler.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.service.FieldValidator;
import team.gif.friendscheduler.service.UserService;

import javax.validation.Valid;

/**
 * TODO: Add validation to ALL input strings (like headers) to disallow special characters
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	
	private final UserService userService;
	private final FieldValidator fieldValidator = new FieldValidator(); // TODO: make this an actual service?
	private static final Logger logger = LogManager.getLogger(UserController.class);
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	
	@PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(
			@Valid @RequestBody User user) {
		
		logger.info("Received createUser request");
		fieldValidator.validateUser(user);
		userService.createUser(user);
		logger.info("Created user " + user.getEmail());
		
		Long token = userService.generateSessionToken(user.getId());
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.header("token", "" + token)
				.body(user);
	}
	
	
	@GetMapping(value = "/user")
	public ResponseEntity<User> getUser(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "email", required = false) String email,
			@RequestHeader("token") Long token) {
		
		logger.info("Received getUser request");
		User result = userService.queryUsers(id, username, email);
		
		if (result == null) {
			result = userService.getUser(userService.getIdFromToken(token));
		}
		
		return ResponseEntity.ok(result);
	}
	
	
	@PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUser(
			@RequestHeader("token") Long token,
			@Valid @RequestBody User user) {
		
		logger.info("Received updateUser request");
		Long id = userService.getIdFromToken(token);
		User result = userService.updateUser(id, user);
		
		return ResponseEntity.ok(result);
	}
	
	
	@DeleteMapping(value = "/user")
	public ResponseEntity<Void> deleteUser(
			@RequestHeader("token") Long token) {
		
		// TODO: Delete their schedule too
		logger.info("Received deleteUser request");
		Long id = userService.getIdFromToken(token);
		userService.deleteUser(id);
		
		return ResponseEntity.noContent().build();
	}
	
}
