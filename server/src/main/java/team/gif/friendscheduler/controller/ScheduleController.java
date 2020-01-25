package team.gif.friendscheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.gif.friendscheduler.model.Interval;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleController {
	
	private final UserService userService;
	
	@Autowired
	public ScheduleController(UserService userService) {
		this.userService = userService;
	}
	
	
	@GetMapping("/schedule/{id}")
	public ResponseEntity<List<Interval>> getSchedule(
			@PathVariable Long id,
			@RequestHeader String token) {
		// TODO: see if target user is in friends list of requester. Throw exception if not
		
		User user = userService.getUser(id);
		
		return ResponseEntity.ok(user.getSchedule());
	}
	
	
	@PutMapping("/schedule")
	public ResponseEntity<Void> addInterval(
			@RequestHeader("token") Long token,
			@RequestBody Interval interval) {
		
		Long id = userService.getIdFromToken(token);
		User user = userService.getUser(id);
		
		user.addInterval(interval);
		userService.saveUser(user);
		
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/schedule")
	public ResponseEntity<Void> removeInterval(
			@RequestHeader("token") Long token,
			@RequestBody Interval interval) {
		
		Long id = userService.getIdFromToken(token);
		User user = userService.getUser(id);
		
		user.removeInterval(interval); // TODO: Throw exception if this fails
		userService.saveUser(user);
		
		return ResponseEntity.ok().build();
	}
	
}
