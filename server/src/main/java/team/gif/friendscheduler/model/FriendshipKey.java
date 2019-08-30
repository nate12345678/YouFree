package team.gif.friendscheduler.model;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class FriendshipKey implements Serializable {
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(nullable = false)
	private User smallerUserId;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(nullable = false)
	private User largerUserId;
	
	
	public FriendshipKey() {}
	
	public FriendshipKey(User smallerUserId, User largerUserId) {
		this.smallerUserId = smallerUserId;
		this.largerUserId = largerUserId;
	}
	
	public User getSmallerUserId() {
		return smallerUserId;
	}
	
	public User getLargerUserId() {
		return largerUserId;
	}
	
	public void setSmallerUserId(User userId) {
		this.smallerUserId = userId;
	}
	
	public void setLargerUserId(User userId) {
		this.largerUserId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		FriendshipKey other = (FriendshipKey) o;
		return this.smallerUserId.equals(other.smallerUserId)
				&& this.largerUserId.equals(other.largerUserId);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
}
