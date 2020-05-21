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
import team.gif.friendscheduler.exception.FriendshipNotFoundException;
import team.gif.friendscheduler.exception.IntervalNotFoundException;
import team.gif.friendscheduler.model.Interval;
import team.gif.friendscheduler.model.User;
import team.gif.friendscheduler.model.request.NewInterval;
import team.gif.friendscheduler.service.AuthService;
import team.gif.friendscheduler.service.FriendshipService;
import team.gif.friendscheduler.service.IntervalService;
import team.gif.friendscheduler.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleController {
	
	private final AuthService authService;
	private final FriendshipService friendshipService;
	private final IntervalService intervalService;
	private final UserService userService;
	private static final Logger logger = LogManager.getLogger(ScheduleController.class);
	
	@Autowired
	public ScheduleController(AuthService authService,
	                          FriendshipService friendshipService,
	                          IntervalService intervalService,
	                          UserService userService) {
		
		this.authService = authService;
		this.friendshipService = friendshipService;
		this.intervalService = intervalService;
		this.userService = userService;
	}
	
	
	@GetMapping("/schedule/{userId}")
	public ResponseEntity<ArrayList<LinkedList<Interval>>> getSchedule(
			@PathVariable Long userId,
			@RequestHeader String token) {
		
		// TODO: validate token
		
		logger.info("Received getSchedule request");
		
		Long requesterId = authService.getUserIdFromToken(token);
		User target = userService.getUser(userId);
		
		if (!friendshipService.hasFriendship(requesterId, userId)) {
			throw new FriendshipNotFoundException(userId);
		}
		
		return ResponseEntity.ok(intervalService.getIntervals(userId));
	}
	
	
	public ResponseEntity<List<ArrayList<LinkedList<Interval>>>> getSchedules(
			@RequestHeader String token,
			@RequestBody LinkedList<Long> userIds) {
		
		// TODO: validate token
	
		logger.info("Received getSchedules request");
		
		long requesterId = authService.getUserIdFromToken(token);
		Long[] friends = friendshipService.getFriends(requesterId).toArray(new Long[0]);
		Arrays.sort(friends);
		
		List<ArrayList<LinkedList<Interval>>> schedules = userIds.stream()
				.filter(targetId -> Arrays.binarySearch(friends, targetId) >= 0) // Filters out non-friends and bad UserIDs
				.map(intervalService::getIntervals) // Maps userIds to schedules
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(schedules);
	}
	
	
	@PutMapping("/schedule")
	public ResponseEntity<ArrayList<LinkedList<Interval>>> addInterval(
			@RequestHeader("token") String token,
			@RequestBody NewInterval interval) {
		
		logger.info("Received addInterval request");
		Long userId = authService.getUserIdFromToken(token);
		User user = userService.getUser(userId);
		intervalService.addInterval(userId, interval);
		
		return ResponseEntity.ok(intervalService.getIntervals(userId));
	}
	
	
	@DeleteMapping("/schedule/{intervalId}")
	public ResponseEntity<ArrayList<LinkedList<Interval>>> removeInterval(
			@PathVariable Long intervalId,
			@RequestHeader("token") String token) {
		
		logger.info("Received removeInterval request: " + intervalId);
		Long userId = authService.getUserIdFromToken(token);
		
		Interval target = intervalService.getInterval(intervalId).orElseThrow(() -> new IntervalNotFoundException(intervalId));
		if (!userId.equals(target.getUserId())) {
			throw new AccessDeniedException(userId, target.getUserId());
		}
		
		intervalService.removeInterval(userId, intervalId);
		
		return ResponseEntity.ok(intervalService.getIntervals(userId));
	}
	
}
