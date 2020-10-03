package team.gif.friendscheduler.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebsocketController {
	
	// There is also @SubscribeMapping
	@MessageMapping("/hello")
	@SendTo("/topic/hello")
	public String sayHello(String message) {
		return "Hello world!";
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
	// TODO: add parameter that takes in a login token
	@SubscribeMapping("/notifications")
	public String subscribeToNotifications() {
		// TODO: return a list of all unacknowledged notifications
		return null;
	}
	

	// TODO: add parameter that takes in a login token
	@MessageMapping("/notifications")
	@SendTo("/topic/hello")
	public String ackNotification(List<Long> notificationIds) {
		// TODO: delete all acknowledged notifications from the backend
		// TODO: somehow indicate to all other clients this is acknowledged
		// We send an ack to all other clients so the notification icon
		// disappears on all the user's devices (if they're logged in on mobile and web, for example
		return null;
	}
	
}
