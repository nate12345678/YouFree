package team.gif.friendscheduler.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FRIENDSHIPS")
public class Friendship {
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(nullable = false)
	private User smallerUserId;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(nullable = false)
	private User largerUserId;
	
	
	public Friendship(User smallerUserId, User largerUserId) {
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
	
}
