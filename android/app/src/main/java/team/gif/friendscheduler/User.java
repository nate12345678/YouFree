package team.gif.friendscheduler;

import com.google.gson.Gson;

public class User {
    long id;
    String username;
    String email;
    String displayName;
    long discordSnowflake;
    int[][] schedule;


    static Gson gson = new Gson();

    public User() {
        this("name");
    }


    public User(String username) {
        this(0, username);
    }

    public User(long id, String username) {
        this(id, username, "none@example.com");
    }

    public User(long id, String username, String email) {
        this(id, username, email, username);
    }

    public User(long id, String username, String email, String displayName) {
        this(id, username, email, displayName, new int[7][96]);
    }

    public User(long id, String username, String email, String displayName, int[][] schedule) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.schedule = schedule;

        this.discordSnowflake = 0;
    }

    static User userFromJson(String json) {
        return gson.fromJson(json, User.class);
    }

    static String userToJson(User user) {
        return gson.toJson(user);
    }

}
