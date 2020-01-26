package team.gif.friendscheduler.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import team.gif.friendscheduler.service.IntervalService;
import team.gif.friendscheduler.service.UserService;

import java.util.ArrayList;
import java.util.LinkedList;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleController {
	
	private final UserService userService;
	private final IntervalService intervalService;
	private static final Logger logger = LogManager.getLogger(ScheduleController.class);
	
	@Autowired
	public ScheduleController(UserService userService, IntervalService intervalService) {
		this.userService = userService;
		this.intervalService = intervalService;
	}
	
	
	@GetMapping("/schedule/{id}")
	public ResponseEntity<ArrayList<LinkedList<Interval>>> getSchedule(
			@PathVariable Long id,
			@RequestHeader String token) {
		// TODO: see if target user is in friends list of requester. Throw exception if not
		
		logger.info("Received getSchedule request");
		
		return ResponseEntity.ok(intervalService.getIntervals(id));
	}
	
	
	@PutMapping("/schedule")
	public ResponseEntity<Void> addInterval(
			@RequestHeader("token") Long token,
			@RequestBody Interval interval) {
		
		logger.info("Received addInterval request");
		Long userId = userService.getIdFromToken(token);
		interval.setUserId(userId);
		intervalService.addInterval(userId, interval);
		
		return ResponseEntity.ok().build();
	}
	
	
	@DeleteMapping("/schedule")
	public ResponseEntity<Void> removeInterval(
			@RequestHeader("token") Long token,
			@RequestBody Interval interval) {
		
		logger.info("Received removeInterval request: " + interval.getId());
		Long userId = userService.getIdFromToken(token);
		
		// TODO: Make sure user can only delete their own intervals
		intervalService.removeInterval(interval);
		
		return ResponseEntity.ok().build();
	}
	
}
