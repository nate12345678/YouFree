package team.gif.friendscheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.service.AuthService;
import team.gif.friendscheduler.service.FriendshipService;
import team.gif.friendscheduler.service.UserService;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class FriendshipController {
	
	private final AuthService authService;
	private final FriendshipService friendshipService;
	private final UserService userService;
	
	@Autowired
	public FriendshipController(AuthService authService, UserService userService, FriendshipService friendshipService) {
		this.authService = authService;
		this.friendshipService = friendshipService;
		this.userService = userService;
	}
	
	
	@GetMapping("/friends")
	public ResponseEntity<List<User>> getFriends(
			@RequestHeader("token") String token) {
		
		User user = userService.getUser(authService.getUserIdFromToken(token));
		List<Long> friendIds = friendshipService.getFriends(user);
		
		List<User> friends = friendIds.stream()
				.map(userService::getUser)
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(friends);
	}
	
	
	@PutMapping("/friends/{id}")
	public ResponseEntity<Void> addFriend(
			@PathVariable Long id,
			@RequestHeader("token") String token) {
		
		Long requesterId = authService.getUserIdFromToken(token);
		friendshipService.addFriendship(requesterId, id);
		
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/friends/{id}")
	public ResponseEntity<Void> removeFriend(
			@PathVariable Long id,
			@RequestHeader("token") String token) {
		
		Long requesterId = authService.getUserIdFromToken(token);
		friendshipService.deleteFriendship(requesterId, id);
		
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/blocked")
	public ResponseEntity<List<User>> getBlocked(
			@RequestHeader("token") String token) {
		
		// TODO: get list of blocked users
		
		List<User> result = new LinkedList<>();
		return ResponseEntity.ok(result);
	}
	
	
	@PutMapping("/blocked/{id}")
	public ResponseEntity<Void> block(
			@PathVariable Long id,
			@RequestHeader("token") String token) {
		
		// TODO: block the user
		
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}
	
	
	@DeleteMapping("/blocked/{id}")
	public ResponseEntity<Void> unblock(
			@PathVariable Long id,
			@RequestHeader("token") String token) {
		
		// TODO: unblock the user
		
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}
	
}
