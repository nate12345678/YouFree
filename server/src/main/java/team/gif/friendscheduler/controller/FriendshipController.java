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
import team.gif.friendscheduler.service.FriendshipService;
import team.gif.friendscheduler.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class FriendshipController {
	
	private UserService userService;
	private FriendshipService friendshipService;
	
	@Autowired
	public FriendshipController(UserService userService, FriendshipService friendshipService) {
		this.userService = userService;
		this.friendshipService = friendshipService;
	}
	
	
	@GetMapping("/friends")
	public ResponseEntity<List<User>> getFriends(
			@RequestHeader("token") Long token) {
		
		User user = userService.getUser(userService.getIdFromToken(token));
		List<Long> friendIds = friendshipService.getFriends(user);
		
		List<User> friends = friendIds.stream()
				.map(friendId -> userService.getUser(friendId))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(friends);
	}
	
	
	@PutMapping("/friends/{id}")
	public ResponseEntity<Void> addFriend(
			@PathVariable Long id,
			@RequestHeader("token") Long token) {
		
		Long requesterId = userService.getIdFromToken(token);
		friendshipService.addFriendship(requesterId, id);
		
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/friends/{id}")
	public ResponseEntity<Void> removeFriend(
			@PathVariable Long id,
			@RequestHeader("token") Long token) {
		
		Long requesterId = userService.getIdFromToken(token);
		friendshipService.deleteFriendship(requesterId, id);
		
		return ResponseEntity.noContent().build();
	}
	
}
