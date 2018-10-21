package team.gif.friendscheduler.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.gif.friendscheduler.UserRepository;
import team.gif.friendscheduler.exception.AccessDeniedException;
import team.gif.friendscheduler.exception.UserNotFoundException;
import team.gif.friendscheduler.model.User;

import java.util.LinkedList;
import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {
	
	@Autowired
	private UserRepository userRepository;
	
	@ApiOperation("Hello World")
	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		return ResponseEntity.ok("Hello world");
	}
	
	
	@ApiOperation("Login")
	@GetMapping("/login")
	public ResponseEntity<User> login(
			@ApiParam @RequestHeader("username") String username,
			@ApiParam @RequestHeader("password") String password) {
		User target = userRepository
				.findUserByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));
		
		if (!password.equals(target.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		return ResponseEntity.status(HttpStatus.OK).header("token", username).body(target);
	}
	
	
	@ApiOperation("Create a new user")
	@PostMapping("/user")
	public ResponseEntity<Void> createUser(
			@ApiParam @RequestHeader("username") String username,
			@ApiParam @RequestHeader("password") String password,
			@ApiParam @RequestHeader("email") String email,
			@ApiParam @RequestHeader("displayName") String displayName) {
		
		User user = new User(username, password, email, displayName);
		
		userRepository.save(user);
		
		return ResponseEntity.created(null).build();
	}
	
	
	@ApiOperation("Get the list of friends of the user currently logged in")
	@GetMapping("/friends")
	public ResponseEntity<List<User>> getFriends(
			@ApiParam @RequestHeader("token") Long token) {
		// Currently, the token is the ID of the user making the request
		Iterable<User> users = userRepository.findAll();
		
		LinkedList<User> list = new LinkedList<>();
		users.forEach(list::addLast);
		
		return ResponseEntity.ok(list);
	}
	
	
	@ApiOperation("Get the schedule of the specified user")
	@GetMapping("/schedule/{id}")
	public ResponseEntity<Integer[][]> getSchedule(
			@ApiParam @PathVariable Long id,
			@ApiParam @RequestHeader String token) {
		// TODO: see if target user is in friends list of requester. Throw exception if not
		
		User user = userRepository
				.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
		
		return ResponseEntity.ok(user.getSchedule());
	}
	
	
	// TODO: uses token to update self, rather than taking ID target
	@ApiOperation("Updates the schedule of the logged-in user")
	@PutMapping("/schedule/{id}")
	public ResponseEntity<Void> updateSchedule(
			@ApiParam @PathVariable Long id,
			@ApiParam @RequestHeader("day") int day,
			@ApiParam @RequestHeader("block") int block,
			@ApiParam @RequestHeader("status") Integer status) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
		user.updateSchedule(day, block, status);
		
		userRepository.save(user);
		
		return ResponseEntity.ok().build();
	}
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
	}
	
}
