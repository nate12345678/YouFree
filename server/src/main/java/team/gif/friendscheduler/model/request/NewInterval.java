package team.gif.friendscheduler.model.request;

import team.gif.friendscheduler.model.Interval;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class NewInterval {
	
	@Min(0) // Monday
	@Max(6) // Sunday
	private Integer dayOfWeek;
	
	@Min(0)
	@Max(Interval.MAX_TIME)
	private Integer startMin;
	
	@Min(0)
	@Max(Interval.MAX_TIME)
	private Integer endMin;
	
	public NewInterval() {
		this.dayOfWeek = 0;
		this.startMin = 0;
		this.endMin = Interval.MAX_TIME;
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
	
	
	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
	
	public void setStartMin(Integer startMin) {
		this.startMin = startMin;
	}
	
	
	public void setEndMin(Integer endMin) {
		this.endMin = endMin;
	}
	
}
