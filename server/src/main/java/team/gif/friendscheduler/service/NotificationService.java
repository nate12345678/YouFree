package team.gif.friendscheduler.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team.gif.friendscheduler.model.notification.FriendRequestNotification;
import team.gif.friendscheduler.repository.NotificationRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
// TODO: add method to delete ALL of a user's notifications, and call it when a user is deleted
public class NotificationService {
	
	private final NotificationRepository notificationRepository;
	private final SimpMessagingTemplate websocket;
	private static final Logger logger = LogManager.getLogger(NotificationService.class);
	
	@Autowired
	public NotificationService(
			NotificationRepository notificationRepository,
			SimpMessagingTemplate websocket
	) {
		this.notificationRepository = notificationRepository;
		this.websocket = websocket;
	}
	
	
	public void sendFriendRequestNotification(Long recipientId, Long requesterId) {
		FriendRequestNotification notification = new FriendRequestNotification(recipientId, requesterId);
		notificationRepository.save(notification);
		
		// Send notification
		String destination = String.format("/app/queue/notifications/%d", recipientId);
		List<FriendRequestNotification> content = List.of(notification);
		websocket.convertAndSend(destination, content);
	}
	
	
	public void deleteFriendRequestNotification(Long recipientId, Long requesterId) {
		notificationRepository.deleteFriendRequestNotificationByRecipientIdAndRequesterId(recipientId, requesterId);
	}
	
	
	// TODO: add userId parameter, so only the recipient can delete their own notifications
	public void deleteFriendRequestNotification(Long notificationId) {
		notificationRepository.deleteById(notificationId);
	}
	
	
	// TODO: add userId parameter, so only the recipient can delete their own notifications
	public void deleteNotifications(List<Long> notificationIds, Long notificationRecipientId) {
		notificationRepository.deleteAllByIdInAndRecipientId(notificationIds, notificationRecipientId);
	}
	
	
	public void deleteAllNotificationsForUser(Long userId) {
		notificationRepository.deleteAllByRecipientId(userId);
	}
	
	
	public List<FriendRequestNotification> getAllNotificationsForUser(Long recipientId) {
		List<FriendRequestNotification> notifications = notificationRepository.getFriendRequestNotificationsByRecipientId(recipientId);
		return notifications;
	}
	
}
