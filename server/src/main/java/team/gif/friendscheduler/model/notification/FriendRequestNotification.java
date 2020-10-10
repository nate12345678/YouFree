package team.gif.friendscheduler.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notifications")
public class FriendRequestNotification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	
	@Column(nullable = false)
	private Long recipientId;
	
	// TODO: combination of recipient and requester should be unique
	@Column(nullable = false)
	private Long requesterId;
	
	
	public FriendRequestNotification() {}
	
	
	public FriendRequestNotification(Long recipientId, Long requesterId) {
		this.recipientId = recipientId;
		this.requesterId = requesterId;
	}
	
	
	public Long getId() {
		return id;
	}
	
	
	public Long getRecipientId() {
		return recipientId;
	}
	
	
	public Long getRequesterId() {
		return requesterId;
	}
	
	
	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}
	
	
	public void setRequesterId(Long requesterId) {
		this.requesterId = requesterId;
	}
	
}
