package team.gif.friendscheduler.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import team.gif.friendscheduler.exception.UnauthorizedException;
import team.gif.friendscheduler.model.notification.FriendRequestNotification;
import team.gif.friendscheduler.service.AuthService;
import team.gif.friendscheduler.service.NotificationService;

import java.util.List;

@Controller
public class WebsocketController {
	
	private final AuthService authService;
	private final NotificationService notificationService;
	private static final Logger logger = LogManager.getLogger(WebsocketController.class);
	
	@Autowired
	public WebsocketController(
			AuthService authService,
			NotificationService notificationService
	) {
		this.authService = authService;
		this.notificationService = notificationService;
	}
	
	
	/*
	 * Here's a possible shape for the response to subscribing to notifications
	 * Each notification should have its own ID
	 * {
	 *   type: "BACKLOG", // Can be "BACKLOG", "PENDING_FRIENDS", "HANGOUT_REQUESTS", etc
	 *   pendingFriends: [
	 *     {
	 *       userId: 1243456, // Add any information necessary for client to display. May only need count for now.
	 *       ...
	 *       notificationId: 12555
	 *     }
	 *   ],
	 *   hangoutRequests: [],
	 *   messages: []
	 * }
	 *
	 */
	@SubscribeMapping("/queue/notifications/{userId}")
	public List<FriendRequestNotification> subscribeToNotifications(
			@DestinationVariable Long userId,
			@Header String token
	) {
		logger.info("Received notification subscription: '/queue/notifications/{}", userId);
		
		authService.validateTokenString(token);
		Long requesterId = authService.getUserIdFromToken(token);
		
		// If the token doesn't match the specified userId, send UNAUTHORIZED response
		if (!requesterId.equals(userId)) {
			throw new UnauthorizedException();
		}
		
		return notificationService.getAllNotificationsForUser(requesterId);
	}
	
}
