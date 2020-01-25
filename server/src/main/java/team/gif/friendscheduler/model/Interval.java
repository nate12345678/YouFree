package team.gif.friendscheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.PositiveOrZero;

@Entity
public class Interval {
	
	public static final int MAX_TIME = 1440; // Number of minutes in a day
	
	@Id
	@PositiveOrZero
	private int start; // Minute of day
	
	@Column
	@Max(MAX_TIME)
	private int end;   // Minute of day
	
	public Interval() {
		this.start = 0;
		this.end = MAX_TIME;
	}
	
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}
	
}
