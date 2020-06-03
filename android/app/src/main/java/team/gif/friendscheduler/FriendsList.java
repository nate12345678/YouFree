package team.gif.friendscheduler;

import com.google.gson.Gson;

public class FriendsList {
	User[] friends;

	static Gson gson = new Gson();

	public static FriendsList friendsListFromJson(String json) {
		return gson.fromJson(json, FriendsList.class);
	}
}
