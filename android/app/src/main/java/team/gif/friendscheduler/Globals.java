package team.gif.friendscheduler;

import okhttp3.MediaType;

public final class Globals {
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static String BASE_URL = "https://youfree.patrickubelhor.com/api/v1";
	static String token;
	static User user;
	static String enteredPass;
	static FriendsList friendsList;
	static boolean stayLoggedOn = false;
	public static final String days[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
}