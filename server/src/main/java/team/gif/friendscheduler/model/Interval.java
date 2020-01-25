package team.gif.friendscheduler.model;

public class Interval {
	
	public static final int MAX_TIME = 1440; // Number of minutes in a day
	
	private int start; // Minute of day
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
