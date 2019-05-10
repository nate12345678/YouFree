package team.gif.friendscheduler;

import com.google.gson.Gson;

public class User {
    long id;
    String username;
    String email;
    long discordSnowflake;
    int[][] schedule;


    static Gson gson = new Gson();

    public User() {
        this("name");
    }


    public User(String username) {
        this(0, username);
    }

    public User(String username, String email) {
	    this(0, username, email);
    }

    public User(long id, String username) {
        this(id, username, "none@example.com");
    }

    public User(long id, String username, String email) {
        this(id, username, email, new int[7][96]);
    }

    public User(long id, String username, String email, int[][] schedule) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.schedule = schedule;

        this.discordSnowflake = 0;
    }

    static User userFromJson(String json) {
        return gson.fromJson(json, User.class);
    }

    static String userToJson(User user) {
//        return gson.toJson(user);

        return "{" +
                "\"discordSnowflake\": \"" + user.discordSnowflake + "\"," +
                "\"username\": \"" + user.username +"\",\n" +
                "\"password\": \"" + Globals.enteredPass + "\",\n" +
                "\"email\": \"" + user.email +"\"\n" +
                "}";
    }

}
