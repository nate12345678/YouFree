package team.gif.friendscheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.gif.friendscheduler.UserRepository;
import team.gif.friendscheduler.exception.AccessDeniedException;
import team.gif.friendscheduler.exception.IncorrectCredentialsException;
import team.gif.friendscheduler.exception.InvalidFieldException;
import team.gif.friendscheduler.exception.UserNotFoundException;
import team.gif.friendscheduler.model.TimeBlock;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.service.FieldValidator;
import team.gif.friendscheduler.service.UserService;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	private FieldValidator fieldValidator = new FieldValidator(); // TODO: make this an actual service?
	
	
	@GetMapping(value = "/hello", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> hello() {
		return ResponseEntity.ok("Hello world");
	}
	
	
	@GetMapping("/login")
	public ResponseEntity<User> login(
			@RequestHeader("username") String username,
			@RequestHeader("password") String password) {
		
		User target = userService.authenticate(username, password);
		Long token = userService.generateSessionToken(target.getId());
		
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", "" + token)
				.body(target);
	}
	
	
	@PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(
			@RequestBody User user) {
		
		fieldValidator.validateUser(user);
		userRepository.save(user);
		
		Long token = userService.generateSessionToken(user.getId());
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.header("token", "" + token)
				.body(user);
	}
	
	
	@GetMapping(value = "/user")
	public ResponseEntity<User> getUser(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "snowflake", required = false) Long discordSnowflake,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "email", required = false) String email,
			@RequestHeader("token") Long token) {
		
		User result = userService.queryUsers(id, discordSnowflake, username, email);
		
		if (result == null) {
			result = userService.getUser(userService.getIdFromToken(token));
		}
	
		return ResponseEntity.ok(result);
	}
	
	
	@PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updateUser(
			@RequestHeader("token") Long token,
			@RequestBody User user) {
		
		Long id = userService.getIdFromToken(token);
		User result = userService.updateUser(id, user);
		
		return ResponseEntity.ok(result);
	}
	
	
	@DeleteMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteUser(
			@RequestHeader("token") Long token) {
		
		Long id = userService.getIdFromToken(token);
		userService.deleteUser(id);
		
		return ResponseEntity.noContent().build();
	}
	
	
	// TODO: Make 'get friends' service
	@GetMapping("/friends")
	public ResponseEntity<List<User>> getFriends(
			@RequestHeader("token") Long token) {
		
		// Currently, the token is the ID of the user making the request
		Iterable<User> users = userRepository.findAll();
		
		LinkedList<User> list = new LinkedList<>();
		users.forEach(list::addLast);
		
		return ResponseEntity.ok(list);
	}
	
	
	@GetMapping("/friends/discord")
	public ResponseEntity<List<Long>> getDiscordFriends(
			@RequestHeader("snowflake") Long snowflake) {
		
		Iterable<User> users = userRepository.findAll();
		
		LinkedList<Long> list = new LinkedList<>();
		users.forEach(user -> list.add(user.getDiscordSnowflake()));
		
		return ResponseEntity.ok(list);
	}
	
	
	@GetMapping("/schedule/{id}")
	public ResponseEntity<int[][]> getSchedule(
			@PathVariable Long id,
			@RequestHeader String token) {
		// TODO: see if target user is in friends list of requester. Throw exception if not
		
		User user = userRepository
				.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
		
		return ResponseEntity.ok(user.getSchedule());
	}
	
	
	@GetMapping("/schedule/discord/{snowflake}")
	public ResponseEntity<int[][]> getDiscordSchedule(
			@PathVariable Long snowflake) {
		
		User user = userRepository
				.findUserByDiscordSnowflake(snowflake)
				.orElseThrow(() -> new UserNotFoundException(snowflake));
		
		return ResponseEntity.ok(user.getSchedule());
	}
	
	
	@PutMapping("/schedule")
	public ResponseEntity<Void> updateSchedule(
			@RequestHeader("token") Long token,
			@RequestBody TimeBlock interval) {
		
		User user = userRepository.findById(token).orElseThrow(() -> new UserNotFoundException(token));
		user.updateSchedule(interval);
		
		userRepository.save(user);
		
		return ResponseEntity.ok().build();
	}
	
	
	@ExceptionHandler
	public ResponseEntity<String> handleInvalidFieldException(InvalidFieldException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}
	
	
	@ExceptionHandler(IncorrectCredentialsException.class)
	public ResponseEntity<String> handleIncorrectCredentialsException(IncorrectCredentialsException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
	}
	
}
