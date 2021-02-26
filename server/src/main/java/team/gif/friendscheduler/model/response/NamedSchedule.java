package team.gif.friendscheduler.model.response;

import team.gif.friendscheduler.model.Interval;

import java.util.ArrayList;
import java.util.LinkedList;

public class NamedSchedule {
	
	private UserResponse user;
	private ArrayList<LinkedList<Interval>> schedule;
	
	
	public NamedSchedule() {}
	
	
	public NamedSchedule(UserResponse user, ArrayList<LinkedList<Interval>> schedule) {
		this.user = user;
		this.schedule = schedule;
	}
	
	
	public UserResponse getUser() {
		return user;
	}
	
	
	public ArrayList<LinkedList<Interval>> getSchedule() {
		return schedule;
	}
	
	
	public void setUser(UserResponse user) {
		this.user = user;
	}
	
	
	public void setSchedule(ArrayList<LinkedList<Interval>> schedule) {
		this.schedule = schedule;
	}
	
}
