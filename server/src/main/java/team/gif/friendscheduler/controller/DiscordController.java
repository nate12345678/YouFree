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
import team.gif.friendscheduler.service.UserService;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO: Add validation to ALL input strings (like headers) to disallow special characters
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiscordController {
	
	private final UserRepository userRepository;
	private final UserService userService;
	
	@Autowired
	public DiscordController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}
	
	
	@GetMapping("/friends/discord")
	public ResponseEntity<List<Long>> getDiscordFriends(
			@RequestHeader("snowflake") Long snowflake) {
		
		Iterable<User> users = userRepository.findAll();
		
		LinkedList<Long> list = new LinkedList<>();
		users.forEach(user -> list.add(user.getDiscordSnowflake()));
		
		return ResponseEntity.ok(list);
	}
	
	
	@GetMapping("/schedule/discord/{snowflake}")
	public ResponseEntity<int[][]> getDiscordSchedule(
			@PathVariable Long snowflake) {
		
		User user = userRepository
				.findUserByDiscordSnowflake(snowflake)
				.orElseThrow(() -> new UserNotFoundException(snowflake));
		
		return ResponseEntity.ok(user.getSchedule());
	}
	
}
