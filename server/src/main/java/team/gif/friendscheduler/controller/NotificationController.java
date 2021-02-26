package team.gif.friendscheduler.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.gif.friendscheduler.model.notification.FriendRequestNotification;
import team.gif.friendscheduler.service.AuthService;
import team.gif.friendscheduler.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {
	
	private static final Logger logger = LogManager.getLogger(NotificationController.class);
	private final AuthService authService;
	private final NotificationService notificationService;
	
	@Autowired
	public NotificationController(
			AuthService authService,
			NotificationService notificationService
	) {
		this.authService = authService;
		this.notificationService = notificationService;
	}
	
	
	@GetMapping("/notifications")
	public ResponseEntity<List<FriendRequestNotification>> getNotifications(
			@RequestHeader("token") String token
	) {
		logger.info("Received getNotifications request");
		authService.validateTokenString(token);
		Long requesterId = authService.getUserIdFromToken(token);
		List<FriendRequestNotification> notifications = notificationService.getAllNotificationsForUser(requesterId);
		
		return ResponseEntity.ok(notifications);
	}
	
	
	@DeleteMapping("/notifications")
	public ResponseEntity<Void> acknowledgeNotifications(
			@RequestHeader("token") String token,
			@RequestBody List<Long> notificationIds
	) {
		logger.info("Received ackNotification request");
		authService.validateTokenString(token);
		Long requesterId = authService.getUserIdFromToken(token);
		
		notificationService.deleteNotifications(notificationIds, requesterId);
		return ResponseEntity.noContent().build();
	}
	
}
