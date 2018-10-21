package team.gif.friendscheduler;

import com.google.gson.Gson;

public class User {
    long id;
    String username;
    String password;
    String email;
    String displayName;
    int[][] schedule;


    static Gson gson = new Gson();

    public User() {
        this("name");
    }

    public User(String username) {
        this(username, "n/a");
    }

    public User(String username, String password) {
        this(0, username, password);
    }

    public User(long id, String username, String password) {
        this(id, username, password, "n/a");
    }

    public User(long id, String username, String password, String email) {
        this(id, username, password, email, username);
    }

    public User(long id, String username, String password, String email, String displayName) {
        this(id, username, password, email, displayName, new int[7][96]);
    }

    public User(long id, String username, String password, String email, String displayName, int[][] schedule) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.displayName = displayName;
        this.schedule = schedule;
    }

    static User userFromJson(String json) {
        return gson.fromJson(json, User.class);
    }

    static String userToJson(User user) {
        return gson.toJson(user);
    }

}
