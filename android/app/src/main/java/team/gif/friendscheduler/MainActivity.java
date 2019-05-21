package team.gif.friendscheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.*;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    NavigationView navigationView;
    DrawerLayout drawer;
    TextView nameNavLabel;
    TextView usernamedNavLabel;
    TextView usernameText;
    TextView emailText;
    TextView snowflakeText;
    ImageView profileImage;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    CoordinatorLayout mainCoordinator;
    View headerview;
    View currentInclude;
    View dailyInclude;
    View scheduleInclude;
    View friendsInclude;
    View profileInclude;
    RecyclerView scheduleRecycler;
    RecyclerView.Adapter scheduleAdapter;
    RecyclerView.LayoutManager scheduleManager;

    RecyclerView friendRecycler;
    RecyclerView.Adapter friendAdapter;
    RecyclerView.LayoutManager friendManager;

    OkHttpClient client = new OkHttpClient();

    Calendar currentTime;

    void getFriends() {
        Request request = new Request.Builder()
                .url(Globals.BASE_URL + "/friends")
                .addHeader("token", Globals.token + "")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "shit");
                if (e.getMessage().contains("Failed to connect")) {
                    Snackbar.make(findViewById(R.id.loginCoordinator),
                            "Lost connection to server", Snackbar.LENGTH_SHORT).show();
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.w("test", "Unexpected code " + response);

                } else {
                    final String friends = response.body().string(); // TODO: convert from JSON to Java object
                    runOnUiThread(() -> {
                        try {
                            JSONArray ja = new JSONArray(friends);
                            Globals.friendsList = new ArrayList<>();
                            for (int i = 0; i < ja.length(); i++) {
                                Globals.friendsList.add(User.userFromJson(ja.getJSONObject(i).toString()));
                            }
                            friendManager = new LinearLayoutManager(MainActivity.this);
                            friendRecycler.setLayoutManager(friendManager);
                            friendAdapter = new FriendAdapter();
                            friendRecycler.setAdapter(friendAdapter);
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to load friends list from database",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    void updateSchedule(int day, int time, int val) {
        Log.w("Ss", Globals.token + "");
        Request request = new Request.Builder()
                .url(Globals.BASE_URL + "/schedule")
                .addHeader("token", Globals.token + "")
                .put(RequestBody.create(Globals.JSON, "{\n" +
                        "\"day\": " + day + ",\n" +
                        "\"block\": " + time + ",\n" +
                        "\"status\": " + val + "\n" +
                        "}"))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "shit");
                if (e.getMessage().contains("Failed to connect")) {
                    Snackbar.make(findViewById(R.id.loginCoordinator),
                            "Lost connection to server", Snackbar.LENGTH_SHORT).show();
                }
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.w("test", "Unexpected code " + response);

                } else {

                }
            }
        });
    }

    void updateProfile(String component) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Write a new value for " + component);
        final EditText input = new EditText(this);
        builder.setView(input);
        builder.setCancelable(true);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                if (!readInput(input).equals("")) {
                    Log.w("test", User.userToJson(Globals.user));
                    Request request = new Request.Builder().url(Globals.BASE_URL + "/user/")
                            .addHeader("token", Globals.token + "")
                            .put(RequestBody.create(Globals.JSON, User.userToJson(Globals.user))).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.w("test", "shit");
                            if (e.getMessage().contains("Failed to connect")) {
                                Snackbar.make(findViewById(R.id.loginCoordinator),
                                        "Lost connection to server", Snackbar.LENGTH_SHORT).show();
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                Log.w("test", "Unexpected code " + response);
                            } else {
                                Globals.user = User.userFromJson(response.body().string());
//                                Log.w("ss", User.userToJson(Globals.))
                            }
                        }
                    });
                } else {
                    Snackbar.make(mainCoordinator, "cancelled", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void setUI() {
        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mainCoordinator = findViewById(R.id.mainCoordinator);
        headerview = navigationView.getHeaderView(0);
        nameNavLabel = headerview.findViewById(R.id.nameNavLabel);
        usernamedNavLabel = headerview.findViewById(R.id.usernameNavLabel);
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        snowflakeText = findViewById(R.id.snowflakeText);
        profileImage = findViewById(R.id.profileImage);
        currentInclude = findViewById(R.id.currentInclude);
        dailyInclude = findViewById(R.id.dailyInclude);
        scheduleInclude = findViewById(R.id.scheduleInclude);
        friendsInclude = findViewById(R.id.friendInclude);
        profileInclude = findViewById(R.id.profileInclude);
        scheduleRecycler = findViewById(R.id.scheduleRecycler);
        friendRecycler = findViewById(R.id.friendRecycler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Current Availability");
        setSupportActionBar(toolbar);

        setUI();

        getFriends();

        fab.setOnClickListener((view) -> {

        });
        usernameText.setOnClickListener((v -> {
            updateProfile("username");
        }));
        emailText.setOnClickListener((v -> {
            updateProfile("email");
        }));

        snowflakeText.setOnClickListener((v -> {
            updateProfile("discordSnowflake");
        }));

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        nameNavLabel.setText(Globals.user.username);
        usernamedNavLabel.setText(Globals.user.username);

        usernameText.setText(Globals.user.username);
        emailText.setText(Globals.user.email);

        currentInclude.setVisibility(View.GONE);
        dailyInclude.setVisibility(View.GONE);
        scheduleInclude.setVisibility(View.GONE);
        profileInclude.setVisibility(View.GONE);
        friendsInclude.setVisibility(View.VISIBLE);

        scheduleRecycler.setHasFixedSize(true);
        scheduleManager = new LinearLayoutManager(this);
        scheduleRecycler.setLayoutManager(scheduleManager);
        scheduleAdapter = new ScheduleAdapter();
        scheduleRecycler.setAdapter(scheduleAdapter);

        friendRecycler.setHasFixedSize(true);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        currentInclude.setVisibility(View.GONE);
        dailyInclude.setVisibility(View.GONE);
        scheduleInclude.setVisibility(View.GONE);
        friendsInclude.setVisibility(View.GONE);
        profileInclude.setVisibility(View.GONE);

        switch (id) {
            case R.id.nav_current:
                getSupportActionBar().setTitle("Current Availability");
                currentInclude.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_daily:
                getSupportActionBar().setTitle("Daily Schedule");
                dailyInclude.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_my_schedule:
                getSupportActionBar().setTitle("My Schedule");
                scheduleInclude.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_friends:
                getSupportActionBar().setTitle("Friends");
                getFriends();
                friendsInclude.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_profile:
                getSupportActionBar().setTitle("My Profile");
                profileInclude.setVisibility(View.VISIBLE);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder


        // Provide a suitable constructor (depends on the kind of dataset)
        public ScheduleAdapter() {
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_line, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.scheduleTimeText.setText(position / 4 + ":" + position % 4 * 15);
            setBusy(holder.mondayView, 0, position);
            setBusy(holder.tuesdayView, 1, position);
            setBusy(holder.wednesdayView, 2, position);
            setBusy(holder.thursdayView, 3, position);
            setBusy(holder.fridayView, 4, position);
            setBusy(holder.saturdayView, 5, position);
            setBusy(holder.sundayView, 6, position);

            holder.mondayView.setOnClickListener((view) -> {
                toggleBusy(holder.mondayView, 0, position);
            });
            holder.tuesdayView.setOnClickListener((view) -> {
                toggleBusy(holder.tuesdayView, 1, position);
            });
            holder.wednesdayView.setOnClickListener((view) -> {
                toggleBusy(holder.wednesdayView, 2, position);
            });
            holder.thursdayView.setOnClickListener((view) -> {
                toggleBusy(holder.thursdayView, 3, position);
            });
            holder.fridayView.setOnClickListener((view) -> {
                toggleBusy(holder.fridayView, 4, position);
            });
            holder.saturdayView.setOnClickListener((view) -> {
                toggleBusy(holder.saturdayView, 5, position);
            });
            holder.sundayView.setOnClickListener((view) -> {
                toggleBusy(holder.sundayView, 6, position);
            });
        }

        void toggleBusy(ImageView v, int day, int pos) {
            switch (Globals.user.schedule[day][pos]) {
                case 0:
                    Globals.user.schedule[day][pos] = 1;
                    v.setBackgroundResource(R.color.red);
                    updateSchedule(day, pos, 1);
                    break;
                case 1:
                    Globals.user.schedule[day][pos] = 0;
                    v.setBackgroundResource(R.color.green);
                    updateSchedule(day, pos, 0);
                    break;
                default:
                    Globals.user.schedule[day][pos] = 0;
                    v.setBackgroundResource(R.color.green);
                    updateSchedule(day, pos, 0);
            }
        }

        void setBusy(ImageView v, int day, int pos) {
            switch (Globals.user.schedule[day][pos]) {
                case 0:
                    v.setBackgroundResource(R.color.green);
                    break;
                case 1:
                    v.setBackgroundResource(R.color.red);
                    break;
                default:
                    v.setBackgroundResource(R.color.green);
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return 96;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            final TextView scheduleTimeText;
            final ImageView mondayView;
            final ImageView tuesdayView;
            final ImageView wednesdayView;
            final ImageView thursdayView;
            final ImageView fridayView;
            final ImageView saturdayView;
            final ImageView sundayView;

            public ViewHolder(View v) {
                super(v);
                scheduleTimeText = v.findViewById(R.id.scheduleTimeText);
                mondayView = v.findViewById(R.id.mondayView);
                tuesdayView = v.findViewById(R.id.tuesdayView);
                wednesdayView = v.findViewById(R.id.wednsdayView);
                thursdayView = v.findViewById(R.id.thursdayView);
                fridayView = v.findViewById(R.id.fridayView);
                saturdayView = v.findViewById(R.id.saturdayView);
                sundayView = v.findViewById(R.id.sundayView);

            }
        }
    }

    public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder


        // Provide a suitable constructor (depends on the kind of dataset)
        public FriendAdapter() {
        }

        // Create new views (invoked by the layout manager)
        @Override
        public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_line, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            currentTime = Calendar.getInstance();
            holder.friendNameText.setText(Globals.friendsList.get(position).username);
            int day = (currentTime.get(Calendar.DAY_OF_WEEK) == 1) ? 6 : currentTime.get(Calendar.DAY_OF_WEEK) - 2;
            int min = currentTime.get(Calendar.MINUTE) / 15;
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            if (Globals.friendsList.get(position).schedule[day][hour * 4 + min] == 0) {
                holder.friendAvailableText.setText("Yes");
            } else {
                holder.friendAvailableText.setText("No");
            }
            for (int i = hour * 4 + min; i < 96; i++) {
                if (Globals.friendsList.get(position).schedule[day][i] == 0) {
                    holder.friendNextAvailText.setText(i / 4 + ":" + i % 4 * 15);
                    break;
                } else if (i == 95) {
                    holder.friendNextAvailText.setText("None");
                }
            }
        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return Globals.friendsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            final TextView friendNameText;
            final TextView friendAvailableText;
            final TextView friendNextAvailText;

            public ViewHolder(View v) {
                super(v);
                friendNameText = v.findViewById(R.id.friendNameText);
                friendAvailableText = v.findViewById(R.id.friendAvailableText);
                friendNextAvailText = v.findViewById(R.id.friendNextAvailText);
            }
        }
    }

    public static String readInput(EditText editText) {
        try {
            return editText.getText().toString();
        } catch (Exception e) {
            return "";
        }
    }
}
