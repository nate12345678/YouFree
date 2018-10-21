package team.gif.friendscheduler;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;

public final class Globals {
    public static final String BASE_URL = "http://216.171.5.211:61235/api/v1";
    static long token;
    static User user;
    static ArrayList<User> friendsList = new ArrayList<>();


    static void makecall(Request request, OkHttpClient client, Activity a) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "shit");

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.w("test", "Unexpected code " + response);

                } else {
                    final String user = response.body().string(); // TODO: convert from JSON to Java object
                    final Long token = Long.parseLong(response.header("token"));
                    Globals.user = User.userFromJson(user);
                    Globals.token = token;
                    Log.w("test", "peanus");
                    a.runOnUiThread(() ->{
                        Globals.user = User.userFromJson(user);
                        Globals.token = token;
                        a.startActivity(new Intent(a.getApplicationContext(), MainActivity.class));
                        Toast.makeText(a, "suck my dick", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }
}