package team.gif.friendscheduler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import team.gif.friendscheduler.model.request.NewInterval;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "intervals")
public class Interval {
	
	public static final int MAX_TIME = 1440; // Number of minutes in a day
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	
	@Column
	private Long userId;
	
	@Column
	@Min(0) // Monday
	@Max(6) // Sunday
	private Integer dayOfWeek;
	
	@Column
	@Min(0)
	@Max(MAX_TIME)
	private Integer startMin;
	
	@Column
	@Min(0)
	@Max(MAX_TIME)
	private Integer endMin;
	
	
	public Interval() {
		this.dayOfWeek = 0;
		this.startMin = 0;
		this.endMin = MAX_TIME;
	}
	
	
	public Interval(Long userId, Integer dayOfWeek, Integer start, Integer end) {
		this.userId = userId;
		this.dayOfWeek = dayOfWeek;
		this.startMin = start;
		this.endMin = end;
	}
	
	
	public Interval(Long userId, NewInterval newInterval) {
		this(userId, newInterval.getDayOfWeek(), newInterval.getStartMin(), newInterval.getEndMin());
	}
	
	
	public Long getId() {
		return id;
	}
	
	
	public Long getUserId() {
		return userId;
	}
	
	
	public Integer getDayOfWeek() {
		return dayOfWeek;
	}
	
	
	public Integer getStartMin() {
		return startMin;
	}
	
	
	public Integer getEndMin() {
		return endMin;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
	
	public void setStartMin(Integer start) {
		this.startMin = start;
	}
	
	
	public void setEndMin(Integer end) {
		this.endMin = end;
	}
	
}
