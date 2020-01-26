package team.gif.friendscheduler.model;

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
	private int dayOfWeek; // Monday = 0, Sunday = 6
	
	@Column
	private int startMin;
	
	@Column
	private int endMin;
	
	
	public Interval() {
		this.dayOfWeek = 0;
		this.startMin = 0;
		this.endMin = MAX_TIME;
	}
	
	public Interval(Long userId, int dayOfWeek, int start, int end) {
		this.userId = userId;
		this.dayOfWeek = dayOfWeek;
		this.startMin = start;
		this.endMin = end;
	}
	
	
	public Long getId() {
		return id;
	}
	
	
	public Long getUserId() {
		return userId;
	}
	
	
	public int getDayOfWeek() {
		return dayOfWeek;
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
	
	
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
	
	public void setStartMin(int start) {
		this.startMin = start;
	}
	
	
	public void setEndMin(int end) {
		this.endMin = end;
	}
	
}
