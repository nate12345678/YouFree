package team.gif.friendscheduler;

import okhttp3.MediaType;

import java.util.ArrayList;

public final class Globals {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String BASE_URL = "http://216.171.4.52:8080/api/v1";
    static long token;
    static User user;
    static ArrayList<User> friendsList = new ArrayList<>();
}