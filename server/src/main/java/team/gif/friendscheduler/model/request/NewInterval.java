package team.gif.friendscheduler.model.request;

import team.gif.friendscheduler.exception.ValidationException;
import team.gif.friendscheduler.model.Interval;

import java.util.LinkedList;
import java.util.List;

public class NewInterval {
	
	private Integer dayOfWeek;
	private Integer startMin;
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
	
	
	public void validate() {
		List<String> errors = new LinkedList<>();
		
		if (dayOfWeek == null) {
			errors.add("dayOfWeek must not be null");
		}
		
		if (startMin == null) {
			errors.add("startMin must not be null");
		}
		
		if (endMin == null) {
			errors.add("endMin must not be null");
		}
		
		if (dayOfWeek != null && (dayOfWeek < 0 || dayOfWeek > 6)) {
			errors.add("dayOfWeek was '" + dayOfWeek + "', but should be in range [0, 6]");
		}
		
		if (startMin != null && (startMin < 0 || startMin > Interval.MAX_TIME)) {
			errors.add("startMin was '" + startMin + "', but should be in range [0, " + Interval.MAX_TIME + "]");
		}
		
		if (endMin != null && (endMin < 0 || endMin > Interval.MAX_TIME)) {
			errors.add("endMin was '" + endMin + "', but should be in range [0, " + Interval.MAX_TIME + "]");
		}
		
		if (startMin != null && endMin != null && startMin >= endMin) {
			errors.add("startMin should be less than endMin");
		}
		
		if (errors.size() == 0) {
			return;
		}
		
		String result = errors.stream().reduce((accumulator, next) -> accumulator + ",\n" + next).get();
		throw new ValidationException(result);
	}
	
}
