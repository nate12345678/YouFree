package team.gif.friendscheduler.controller;

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
import org.springframework.web.bind.annotation.RestController;
import team.gif.friendscheduler.UserRepository;
import team.gif.friendscheduler.exception.AccessDeniedException;
import team.gif.friendscheduler.exception.UserNotFoundException;
import team.gif.friendscheduler.model.User;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		return ResponseEntity.ok("Hello world");
	}
	
	
	@GetMapping("/login")
	public ResponseEntity<User> login(
			@RequestHeader("username") String username,
			@RequestHeader("password") String password) {
		User target = userRepository
				.findUserByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));
		
		if (!password.equals(target.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		return ResponseEntity.status(HttpStatus.OK).header("token", "" + target.getId()).body(target);
	}
	
	
	@PostMapping("/user")
	public ResponseEntity<Void> createUser(
			@RequestHeader("username") String username,
			@RequestHeader("password") String password,
			@RequestHeader("email") String email,
			@RequestHeader("displayName") String displayName) {
		
		User user = new User(username, password, email, displayName);
		
		userRepository.save(user);
		
		return ResponseEntity.created(null).build();
	}
	
	
	@GetMapping("/friends")
	public ResponseEntity<List<User>> getFriends(
			@RequestHeader("token") Long token) {
		
		// Currently, the token is the ID of the user making the request
		Iterable<User> users = userRepository.findAll();
		
		LinkedList<User> list = new LinkedList<>();
		users.forEach(list::addLast);
		
		return ResponseEntity.ok(list);
	}
	
	
	@GetMapping("/schedule/{id}")
	public ResponseEntity<Integer[][]> getSchedule(
			@PathVariable Long id,
			@RequestHeader String token) {
		// TODO: see if target user is in friends list of requester. Throw exception if not
		
		User user = userRepository
				.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
		
		return ResponseEntity.ok(user.getSchedule());
	}
	
	
	// TODO: uses token to update self, rather than taking ID target
	@PutMapping("/schedule")
	public ResponseEntity<Void> updateSchedule(
			@RequestHeader("token") Long token,
			@RequestHeader("day") int day,
			@RequestHeader("block") int block,
			@RequestHeader("status") Integer status) {
		
		User user = userRepository.findById(token).orElseThrow(() -> new UserNotFoundException(token));
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
