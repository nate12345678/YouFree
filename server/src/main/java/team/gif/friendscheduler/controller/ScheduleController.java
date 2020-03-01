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
import team.gif.friendscheduler.exception.AccessDeniedException;
import team.gif.friendscheduler.exception.IntervalNotFoundException;
import team.gif.friendscheduler.model.Interval;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.model.request.NewInterval;
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
	
	
	@GetMapping("/schedule/{userId}")
	public ResponseEntity<ArrayList<LinkedList<Interval>>> getSchedule(
			@PathVariable Long userId,
			@RequestHeader String token) {
		// TODO: see if target user is in friends list of requester. Throw exception if not
		
		logger.info("Received getSchedule request");
		
		User target = userService.getUser(userId);
		
		return ResponseEntity.ok(intervalService.getIntervals(userId));
	}
	
	
	@PutMapping("/schedule")
	public ResponseEntity<ArrayList<LinkedList<Interval>>> addInterval(
			@RequestHeader("token") Long token,
			@RequestBody NewInterval interval) {
		
		logger.info("Received addInterval request");
		Long userId = userService.getIdFromToken(token);
		User user = userService.getUser(userId);
		intervalService.addInterval(userId, interval);
		
		return ResponseEntity.ok(intervalService.getIntervals(userId));
	}
	
	
	@DeleteMapping("/schedule/{intervalId}")
	public ResponseEntity<ArrayList<LinkedList<Interval>>> removeInterval(
			@PathVariable Long intervalId,
			@RequestHeader("token") Long token) {
		
		logger.info("Received removeInterval request: " + intervalId);
		Long userId = userService.getIdFromToken(token);
		
		Interval target = intervalService.getInterval(intervalId).orElseThrow(() -> new IntervalNotFoundException(intervalId));
		if (!userId.equals(target.getUserId())) {
			throw new AccessDeniedException(userId, target.getUserId());
		}
		
		intervalService.removeInterval(userId, intervalId);
		
		return ResponseEntity.ok(intervalService.getIntervals(userId));
	}
	
}
