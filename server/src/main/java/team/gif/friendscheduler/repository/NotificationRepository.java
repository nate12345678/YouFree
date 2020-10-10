package team.gif.friendscheduler.repository;

import org.springframework.data.repository.CrudRepository;
import team.gif.friendscheduler.model.notification.FriendRequestNotification;

import java.util.List;

public interface NotificationRepository extends CrudRepository<FriendRequestNotification, Long> {
	
	void deleteFriendRequestNotificationByRecipientIdAndRequesterId(Long recipientId, Long requesterId);
	void deleteAllByIdIn(List<Long> notificationIds);
	void deleteAllByRecipientId(Long recipientId);
	List<FriendRequestNotification> getFriendRequestNotificationsByRecipientId(Long recipientId);
	
}
