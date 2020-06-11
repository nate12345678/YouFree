package team.gif.friendscheduler.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This represents a relationship between two Users.
 */
@Entity
@Table(name = "friendships")
public class Friendship {
	
	@EmbeddedId
	private FriendshipKey friendshipKey;
	
	@Column
	private FriendshipStatus status;
	
	
	public Friendship() {}
	
	
	public Friendship(Long smallerUserId, Long largerUserId, FriendshipStatus status) {
		this(new FriendshipKey(smallerUserId, largerUserId), status);
	}
	
	
	public Friendship(FriendshipKey friendshipKey, FriendshipStatus status) {
		this.friendshipKey = friendshipKey;
		this.status = status;
	}
	
	
	public FriendshipKey getFriendshipKey() {
		return friendshipKey;
	}
	
	
	public FriendshipStatus getStatus() {
		return status;
	}
	
	
	public void setFriendshipKey(FriendshipKey friendshipKey) {
		this.friendshipKey = friendshipKey;
	}
	
	
	public void setStatus(FriendshipStatus status) {
		this.status = status;
	}
	
}
