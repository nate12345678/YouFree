package team.gif.friendscheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	
	@PutMapping("/friends/{userId}")
	public ResponseEntity<Void> addFriend(
			@PathVariable Long userId,
			@RequestHeader("token") String token) {
		
		Long requesterId = authService.getUserIdFromToken(token);
		friendshipService.addFriendship(requesterId, userId);
		
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/friends/{userId}")
	public ResponseEntity<Void> removeFriend(
			@PathVariable Long userId,
			@RequestHeader("token") String token) {
		
		Long requesterId = authService.getUserIdFromToken(token);
		friendshipService.deleteFriendship(requesterId, userId);
		
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("/friends/pending")
	public ResponseEntity<List<User>> getPendingRequests(
			@RequestHeader("token") String token) {
		
		authService.validateTokenString(token);
		Long requesterId = authService.getUserIdFromToken(token);
		
		List<User> friendRequests = friendshipService.getPendingRequests(requesterId)
				.stream()
				.map(userService::getUser)
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(friendRequests);
	}
	
	
	@GetMapping("/friends/sent")
	public ResponseEntity<List<User>> getSentRequests(
			@RequestHeader("token") String token) {
		
		authService.validateTokenString(token);
		Long requesterId = authService.getUserIdFromToken(token);
		
		List<User> requests = friendshipService.getSentRequests(requesterId)
				.stream()
				.map(userService::getUser)
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(requests);
	}
	
	
	@GetMapping("/blocked")
	public ResponseEntity<List<User>> getBlocked(
			@RequestHeader("token") String token) {
		
		authService.validateTokenString(token);
		Long requesterId = authService.getUserIdFromToken(token);
		
		List<User> result = friendshipService.getBlockedUsers(requesterId)
				.stream()
				.map(userService::getUser)
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(result);
	}
	
	
	@PutMapping("/blocked/{userId}")
	public ResponseEntity<Void> block(
			@PathVariable Long userId,
			@RequestHeader("token") String token) {
		
		// TODO: Verify target user exists (this *should* be handled by database)
		authService.validateTokenString(token);
		Long requesterId = authService.getUserIdFromToken(token);
		friendshipService.block(requesterId, userId);
		
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/blocked/{userId}")
	public ResponseEntity<Void> unblock(
			@PathVariable Long userId,
			@RequestHeader("token") String token) {
		
		// TODO: Verify target user exists (this *should* be handled by database)
		authService.validateTokenString(token);
		Long requesterId = authService.getUserIdFromToken(token);
		friendshipService.unblock(requesterId, userId);
		
		return ResponseEntity.ok().build();
	}
	
}
