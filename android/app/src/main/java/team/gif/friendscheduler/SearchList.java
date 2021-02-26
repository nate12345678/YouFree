package team.gif.friendscheduler;

import com.google.gson.Gson;

public class SearchList {
    User[] results;

    static Gson gson = new Gson();

    String[] toStringArray() {
        String[] out = new String[results.length];
        for(int i = 0; i < results.length; i++) {
            out[i] = results[i].username;
        }
        return out;
    }


    static SearchList searchListFromJson(String json) {
        return gson.fromJson(json, SearchList.class);
    }
}
