package team.gif.friendscheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	
}
