package team.gif.friendscheduler.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class FriendshipKey implements Serializable {
	
	@Column
	private Long smallerUserId;
	
	@Column
	private Long largerUserId;
	
	
	public FriendshipKey() {}
	
	public FriendshipKey(Long smallerUserId, Long largerUserId) {
		this.smallerUserId = smallerUserId;
		this.largerUserId = largerUserId;
		
		if (smallerUserId > largerUserId)
			throw new IllegalArgumentException("First argument must have a smaller User ID than second argument!");
	}
	
	public Long getSmallerUserId() {
		return smallerUserId;
	}
	
	public Long getLargerUserId() {
		return largerUserId;
	}
	
	public void setSmallerUserId(Long userId) {
		this.smallerUserId = userId;
	}
	
	public void setLargerUserId(Long userId) {
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
