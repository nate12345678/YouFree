package team.gif.friendscheduler.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.service.UserService;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
	
	private final UserService userService;
	private static final Logger logger = LogManager.getLogger(AuthController.class);
	
	@Autowired
	public AuthController(UserService userService) {
		this.userService = userService;
	}
	
	
	@GetMapping("/login")
	public ResponseEntity<User> login(
			@RequestHeader("email") String email,
			@RequestHeader("password") String password) {
		
		logger.info("Received login request");
		User target = userService.authenticate(email, password);
		Long token = userService.generateSessionToken(target.getId());
		
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", "" + token)
				.body(target);
	}
	
}
