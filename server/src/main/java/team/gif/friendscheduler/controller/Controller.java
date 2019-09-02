package team.gif.friendscheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.gif.friendscheduler.exception.UserNotFoundException;
import team.gif.friendscheduler.model.TimeBlock;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.repository.UserRepository;
import team.gif.friendscheduler.service.FriendshipService;
import team.gif.friendscheduler.service.UserService;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO: Add validation to ALL input strings (like headers) to disallow special characters
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {
	
	private final FriendshipService friendshipService;
	private final UserRepository userRepository;
	private final UserService userService;
	
	@Autowired
	public Controller(FriendshipService friendshipService, UserRepository userRepository, UserService userService) {
		this.friendshipService = friendshipService;
		this.userRepository = userRepository;
		this.userService = userService;
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
	
	
	@GetMapping("/friends")
	public ResponseEntity<List<User>> getFriends(
			@RequestHeader("token") Long token) {
		
		User user = userService.getUser(userService.getIdFromToken(token));
		List<User> list = friendshipService.getFriends(user);
		
		return ResponseEntity.ok(list);
	}
	
	
	@PutMapping("/friends/{id}")
	public ResponseEntity<Void> addFriend(
			@PathVariable Long id,
			@RequestHeader("token") Long token) {
		
		User requester = userService.getUser(userService.getIdFromToken(token));
		User target = userService.getUser(id);
		friendshipService.addFriendship(requester, target);
		
		return ResponseEntity.ok().build();
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
		
		User user = userService.getUser(id);
		
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
		
		Long id = userService.getIdFromToken(token);
		User user = userService.getUser(id);
		
		user.updateSchedule(interval);
		
		userService.saveUser(user);
		
		return ResponseEntity.ok().build();
	}
	
}
