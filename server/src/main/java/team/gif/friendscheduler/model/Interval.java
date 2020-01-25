package team.gif.friendscheduler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Intervals")
public class Interval {
	
	public static final int MAX_TIME = 1440; // Number of minutes in a day
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private Long userId;
	
	@Column
	private int startMin;
	
	@Column
	private int endMin;
	
	
	public Interval() {
		this.startMin = 0;
		this.endMin = MAX_TIME;
	}
	
	public Interval(Long userId, int start, int end) {
		this.userId = userId;
		this.startMin = start;
		this.endMin = end;
	}
	
	
	public Long getId() {
		return id;
	}
	
	
	public Long getUserId() {
		return userId;
	}
	
	
	public int getStartMin() {
		return startMin;
	}
	
	
	public int getEndMin() {
		return endMin;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	public void setStartMin(int start) {
		this.startMin = start;
	}
	
	
	public void setEndMin(int end) {
		this.endMin = end;
	}
	
}
