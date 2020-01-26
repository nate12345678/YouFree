package team.gif.friendscheduler;

import com.google.gson.Gson;

public class User {
	long id;
	String username;
	String email;
	String schedule[][];


	static Gson gson = new Gson();

	public User() {
		this("name", "email@example.com");
	}


	public User(String name, String email) {
		this.username = name;
		this.email = email;
	}


	public User(long id, String username, String email, String schedule[][]) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.schedule = schedule;

	}

	static User userFromJson(String json) {
		return gson.fromJson(json, User.class);
	}

	static String userToJson(User user) {
		return "{" +
				"\"username\": \"" + user.username + "\",\n" +
				"\"password\": \"" + Globals.enteredPass + "\",\n" +
				"\"email\": \"" + user.email + "\"\n" +
				"}";
	}

	static String autoUserToJson(User user) {
		return gson.toJson(user);
	}


}
