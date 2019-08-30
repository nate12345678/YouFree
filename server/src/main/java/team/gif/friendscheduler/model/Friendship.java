package team.gif.friendscheduler.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This represents a relationship between two Users.
 */
@Entity
@Table(name = "FRIENDSHIPS")
public class Friendship {
	
	@EmbeddedId
	private FriendshipKey friendshipKey;
	
	@Column
	private int status;
	
	
	public Friendship() {}
	
	
	public Friendship(User smallerUserId, User largerUserId, int status) {
		this(new FriendshipKey(smallerUserId, largerUserId), status);
	}
	
	
	public Friendship(FriendshipKey friendshipKey, int status) {
		this.friendshipKey = friendshipKey;
		this.status = status;
	}
	
	
	public FriendshipKey getFriendshipKey() {
		return friendshipKey;
	}
	
	
	public int getStatus() {
		return status;
	}
	
	
	public void setFriendshipKey(FriendshipKey friendshipKey) {
		this.friendshipKey = friendshipKey;
	}
	
	
	public void setStatus(int status) {
		this.status = status;
	}
	
}
