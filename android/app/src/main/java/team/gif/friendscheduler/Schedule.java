package team.gif.friendscheduler;

import com.google.gson.Gson;

public class Schedule {

	Interval[][] schedule;

	static Gson gson = new Gson();

	public static Schedule scheduleFromJson(String json) {
		return gson.fromJson(json, Schedule.class);
	}

	public static class Interval {
		long id;
		long userId;
		int dayOfWeek;
		int startMin;
		int endMin;
	}


}
