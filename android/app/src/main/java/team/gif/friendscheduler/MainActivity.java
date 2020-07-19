
package team.gif.friendscheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import team.gif.greyshiftedapi.ExtendedActivity;

public class MainActivity extends ExtendedActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    NavigationView navigationView;
    DrawerLayout drawer;
    EditText searchEditText;
    TextView usernameNavLabel;
    TextView emailNavLabel;
    TextView usernameText;
    TextView emailText;
    TextView userIDText;
    ImageView profileImage;
    ImageView searchButton;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    CoordinatorLayout mainCoordinator;
    View headerView;
    View currentInclude;
    View dailyInclude;
    View scheduleInclude;
    View friendsInclude;
    View profileInclude;
    View searchInclude;
    ConstraintLayout mondayLayout;
    ConstraintLayout tuesdayLayout;
    ConstraintLayout wednesdayLayout;
    ConstraintLayout thursdayLayout;
    ConstraintLayout fridayLayout;
    ConstraintLayout saturdayLayout;
    ConstraintLayout sundayLayout;

    RecyclerView friendRecycler;
    RecyclerView.Adapter friendAdapter;
    RecyclerView.LayoutManager friendManager;

    RecyclerView searchRecycler;
    RecyclerView.Adapter searchAdapter;
    RecyclerView.LayoutManager searchManager;

    OkHttpClient client = new OkHttpClient();

    Calendar currentTime;

    HashMap<String, Long> intervalIds = new HashMap<>();

    int day;
    int startTime;
    int endTime;
    int startHour;
    int startMin;
    double scale;

    AtomicInteger friendScheduleLoadedCount = new AtomicInteger(0);

    final int[] idxToRes = {R.id.mondayLayout, R.id.tuesdayLayout, R.id.wednesdayLayout, R.id.thursdayLayout,
            R.id.fridayLayout, R.id.saturdayLayout, R.id.sundayLayout};

    SearchList results;

    void logOut() {
        Request request = new Request.Builder()
                .url(Globals.BASE_URL + "/logout")
                .addHeader("token", Globals.token + "")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "failed to make request");
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
                    runOnUiThread(() -> {
                        Snackbar.make(mainCoordinator, "Failed to log out", Snackbar.LENGTH_LONG).show();
                    });
                } else {
                    Globals.token = "";
                    Globals.user = new User();
                    Globals.enteredPass = "";
                    SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    prefs.edit().remove("password").apply();
                    prefs.edit().remove("email").apply();
                    prefs.edit().remove("stayLoggedOn").apply();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }
            }
        });
    }

    void toSettings() {

    }

    // FRIEND PAGE
    void getFriends() {
        Request request = new Request.Builder()
                .url(Globals.BASE_URL + "/friends")
                .addHeader("token", Globals.token + "")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "failed to make request");
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
                    final String body = response.body().string(); // TODO: convert from JSON to Java object
                    response.body().close();
                    Globals.friendsList = FriendsList.friendsListFromJson("{\"friends\":" + body + "}");
                    getFriendSchedule();
                    runOnUiThread(() -> {
                        try {
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

    void getFriendSchedule() {
        for (int i = 0; i < Globals.friendsList.friends.length; i++) {
            final int I_FINAL = i;
            Request request = new Request.Builder()
                    .url(Globals.BASE_URL + "/schedule/" + Globals.friendsList.friends[i].id)
                    .addHeader("token", Globals.token + "")
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.w("test", "failed to make request");
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
                        String body = "{\"schedule\":" + response.body().string() + "}";
                        Globals.friendsList.friends[I_FINAL].schedule = Schedule.scheduleFromJson(body);
                        response.body().close();
                        if (friendScheduleLoadedCount.incrementAndGet() == Globals.friendsList.friends.length) {
                            runOnUiThread(() -> {
                                Log.w("friends", "All friends loaded");
                            });
                        }
                    }
                }
            });
        }
    }

    void makeFriendRequest(long id) {
        Request request = new Request.Builder()
                .url(Globals.BASE_URL + "/friends/" + id)
                .addHeader("token", Globals.token + "")
                .put(RequestBody.create(Globals.JSON, ""))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "failed to make request");
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
                    Log.w("test", "friend request made");
                }
            }
        });
    }

    void searchForFriends() {
        String term = readInput(searchEditText);
        Request request = new Request.Builder().url(Globals.BASE_URL + "/search/")
                .addHeader("token", Globals.token + "")
                .addHeader("query", term)
                .get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "failed to make request");
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
                    final String body = response.body().string(); // TODO: convert from JSON to Java object
                    response.body().close();
                    results = SearchList.searchListFromJson("{\"results\":" + body + "}");
                    runOnUiThread(() -> {
                        try {
                            searchManager = new LinearLayoutManager(MainActivity.this);
                            searchRecycler.setLayoutManager(searchManager);
                            searchAdapter = new SearchAdapter();
                            searchRecycler.setAdapter(searchAdapter);
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to load friends list from database",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    // SCHEDULE PAGE
    void updateUserRequest() {
        Request request = new Request.Builder().url(Globals.BASE_URL + "/user/")
                .addHeader("token", Globals.token + "")
                .put(RequestBody.create(Globals.JSON, User.userToJson(Globals.user))).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "failed to make request");
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
                    response.body().close();
                    Log.w("success! code ", response.code() + "");
                }
            }
        });
    }

    void updateSchedule() {
        Request request = new Request.Builder()
                .url(Globals.BASE_URL + "/schedule")
                .addHeader("token", Globals.token + "")
                .put(RequestBody.create(Globals.JSON, "{\n" +
                        "\"dayOfWeek\": " + day + ",\n" +
                        "\"startMin\": " + startTime + ",\n" +
                        "\"endMin\": " + endTime + "\n" +
                        "}"))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "failed to make request");
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
                    fillSchedule();
                }
            }
        });
    }

    void removeScheduleItem(long intervalId) {
        Request request = new Request.Builder()
                .url(Globals.BASE_URL + "/schedule/" + intervalId)
                .addHeader("token", Globals.token + "")
                .delete()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "failed to make request");
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
                    fillSchedule();
                }
            }
        });
    }

    void addFreeTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final Spinner daySpinner = new Spinner(this);
        daySpinner.setPadding(32, 4, 32, 4);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Globals.days);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(spinnerArrayAdapter);

        builder.setTitle("New Free Time")
                .setMessage("Choose a day of the week")
                .setView(daySpinner)
                .setPositiveButton("Okay", (dialog, which) -> {
                    day = daySpinner.getSelectedItemPosition();
                    dialog.cancel();
                    getStartTime();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                });

        AlertDialog dayDialog = builder.create();
        dayDialog.show();

    }

    void getStartTime() {
        Dialog timePicker = new TimePickerDialog(this, (TimePicker view, int hourOfDay, int minute) -> {
            startHour = hourOfDay;
            startMin = minute;
            startTime = startHour * 60 + startMin;
            getEndTime();
        }, 0, 0, false);
        timePicker.show();

    }

    void getEndTime() {
        Dialog timePicker = new TimePickerDialog(this, (TimePicker view, int hourOfDay, int minute) -> {
            endTime = hourOfDay * 60 + minute;
            if (endTime <= startTime) {
                Snackbar.make(mainCoordinator, "End time cannot be before start", Snackbar.LENGTH_SHORT).show();
            } else {
                updateSchedule();
            }
        }, startHour, startMin, false);
        timePicker.show();
    }

    TextView createTimeBox(int day, int start, int end, long id) {
        TextView box = new TextView(getApplicationContext());

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(0, 0);

        params.height = (int) (((float) (end - start)) * scale);

        params.leftToLeft = idxToRes[day];
        params.rightToRight = idxToRes[day];
        params.topToTop = idxToRes[day];
        params.topMargin = (int) (((float) (start + 8)) * scale);
        box.setBackgroundColor(getColor(R.color.blue));
        box.setText("" + id);
        box.setTextColor(getColor(R.color.blue));
        box.setOnLongClickListener(new RemoveClickLister());
        box.setLayoutParams(params);

        return box;
    }

    void fillSchedule() {
        runOnUiThread(() -> {
            for (int i = 0; i < 7; i++) {
                ((ConstraintLayout) findViewById(idxToRes[i])).removeAllViews();
            }
            intervalIds.clear();
        });
        Request request = new Request.Builder()
                .url(Globals.BASE_URL + "/schedule/" + Globals.user.id)
                .addHeader("token", Globals.token + "")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("test", "failed to make request");
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
                    String body = "{\"schedule\":" + response.body().string() + "}";
                    response.body().close();
                    Globals.user.schedule = Schedule.scheduleFromJson(body);
                    runOnUiThread(() -> {
                        for (int i = 0; i < Globals.user.schedule.schedule.length; i++) {
                            ConstraintLayout currRow = findViewById(idxToRes[i]);
                            for (int j = 0; j < Globals.user.schedule.schedule[i].length; j++) {
                                Schedule.Interval curr = Globals.user.schedule.schedule[i][j];
                                TextView box = createTimeBox(curr.dayOfWeek, curr.startMin, curr.endMin, curr.id);
                                currRow.addView(box);
                                intervalIds.put(box.getText().toString(), curr.id);

                            }
                        }
                    });
                }
            }
        });
    }


    // USER PAGE
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
                    if ("email".equals(component)) {
                        Globals.user.email = readInput(input);
                        emailText.setText(Globals.user.email);
                    }
                    updateUserRequest();
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

    void newProfileImage() {
        Toast.makeText(this, "O GOD O FUK", Toast.LENGTH_SHORT).show();
    }


    // META FUNCTIONS
    void fabClick() {
        if (profileInclude.getVisibility() == View.VISIBLE) {
            updateUserRequest();
        } else if (scheduleInclude.getVisibility() == View.VISIBLE) {
            addFreeTime();
        } else if (friendsInclude.getVisibility() == View.VISIBLE) {
            friendsInclude.setVisibility(View.GONE);
            searchInclude.setVisibility(View.VISIBLE);
        }
    }

    void setUI() {
        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mainCoordinator = findViewById(R.id.mainCoordinator);
        headerView = navigationView.getHeaderView(0);
        usernameNavLabel = headerView.findViewById(R.id.usernameNavLabel);
        emailNavLabel = headerView.findViewById(R.id.emailLabel);
        usernameText = findViewById(R.id.emailText);
        emailText = findViewById(R.id.usernameText);
        userIDText = findViewById(R.id.UserIDText);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        profileImage = findViewById(R.id.profileImage);
        currentInclude = findViewById(R.id.currentInclude);
        dailyInclude = findViewById(R.id.dailyInclude);
        scheduleInclude = findViewById(R.id.scheduleInclude);
        friendsInclude = findViewById(R.id.friendInclude);
        profileInclude = findViewById(R.id.profileInclude);
        searchInclude = findViewById(R.id.searchInclude);
        friendRecycler = findViewById(R.id.friendRecycler);
        searchRecycler = findViewById(R.id.searchRecycler);
        mondayLayout = findViewById(R.id.mondayLayout);
        tuesdayLayout = findViewById(R.id.tuesdayLayout);
        wednesdayLayout = findViewById(R.id.wednesdayLayout);
        thursdayLayout = findViewById(R.id.thursdayLayout);
        fridayLayout = findViewById(R.id.fridayLayout);
        saturdayLayout = findViewById(R.id.saturdayLayout);
        sundayLayout = findViewById(R.id.sundayLayout);
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

//		scale = getResources().getDisplayMetrics().density;
        scale = 3;

        fab.setOnClickListener((view) -> {
            fabClick();
        });
        usernameText.setOnClickListener((v -> {
            updateProfile("username");
        }));
        emailText.setOnClickListener((v -> {
            updateProfile("email");
        }));
        searchButton.setOnClickListener((v -> {
            searchForFriends();
        }));

        profileImage.setOnClickListener((v -> {
            newProfileImage();
        }));

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        usernameNavLabel.setText(Globals.user.username);
        emailNavLabel.setText(Globals.user.email);

        usernameText.setText(Globals.user.username);
        emailText.setText(Globals.user.email);
        userIDText.setText(Globals.user.id + "");

        currentInclude.setVisibility(View.GONE);
        dailyInclude.setVisibility(View.GONE);
        scheduleInclude.setVisibility(View.GONE);
        profileInclude.setVisibility(View.GONE);
        friendsInclude.setVisibility(View.VISIBLE);
        searchInclude.setVisibility(View.GONE);

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

        switch (id) {
            case R.id.action_settings:
                toSettings();
                return true;
            case R.id.action_logout:
                logOut();
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
        searchInclude.setVisibility(View.GONE);

        fab.setVisibility(View.VISIBLE);
        fab.setForeground(getDrawable(R.drawable.ic_add_white_24dp));

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
                fillSchedule();
                break;
            case R.id.nav_friends:
                getSupportActionBar().setTitle("Friends");
                getFriends();
                friendsInclude.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_profile:
                getSupportActionBar().setTitle("My Profile");
                fab.setForeground(getDrawable(R.drawable.baseline_save_white_24));
                profileInclude.setVisibility(View.VISIBLE);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // INTERNAL CLASSES
    public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        TextView createFriendTimeBox(ConstraintLayout c, int start, int end) {
            return null;
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public FriendAdapter() {
        }

        // Create new views (invoked by the layout manager)
        @Override
        public FriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_avail_line, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            currentTime = Calendar.getInstance();
            holder.friendNameText.setText(Globals.friendsList.friends[position].username);
            int day = (currentTime.get(Calendar.DAY_OF_WEEK) == 1) ? 6 : currentTime.get(Calendar.DAY_OF_WEEK) - 2;
            int min = currentTime.get(Calendar.MINUTE) / 15;
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
//			Schedule.Interval[] today = Globals.friendsList.friends[position].schedule.schedule[day];
//			for (int i = 0; i < today.length; i++) {
//				TextView box;
//				if(today[i].startMin > hour * 60 + min && today[i].startMin < (hour + 3) * 60 + min) {
//					//startmin is in interval, endmin is in interval or future
//					if(today[i].endMin < (hour + 3) * 60 + min) {
//						//wholly inside interval
//						box = createFriendTimeBox(holder.rowLayout,
//								today[i].startMin - (hour*60 + min), today[i].endMin - (hour * 60 + min));
//					} else {
//						//ends after interval
//						box = createFriendTimeBox(holder.rowLayout,
//								today[i].startMin - (hour*60 + min), 3 * 60);
//					}
//				} else if(today[i].endMin > hour * 60 + min && today[i].endMin < (hour + 3) * 60 + min) {
//					//endmin is in interval, but start is in past
//					box = createFriendTimeBox(holder.rowLayout, 0, today[i].endMin - (hour * 60 + min));
//				} else if(today[i].startMin < hour * 60 + min && today[i].endMin > (hour + 3) * 60 + min) {
//					//startmin in past, endmin in future
//					box = createFriendTimeBox(holder.rowLayout, 0, 3 * 60);
//				}
//			}

        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return Globals.friendsList.friends.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            final TextView friendNameText;
            final ConstraintLayout rowLayout;


            public ViewHolder(View v) {
                super(v);
                friendNameText = v.findViewById(R.id.friendNameText);
                rowLayout = v.findViewById(R.id.friendAvailLineLayout);

            }
        }
    }


    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

        public SearchAdapter() {

        }

        @Override
        public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_line, parent, false);

            SearchAdapter.ViewHolder vh = new SearchAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.searchUsernameText.setText(results.results[position].username);
            holder.searchEmailText.setText(results.results[position].email);
        }

        @Override
        public int getItemCount() {
            return results.results.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            final TextView searchUsernameText;
            final TextView searchEmailText;
            final ImageView searchProfileImage;
            final ConstraintLayout searchLineLayout;


            public ViewHolder(View v) {
                super(v);
                searchUsernameText = v.findViewById(R.id.searchUsernameText);
                searchEmailText = v.findViewById(R.id.searchEmailText);
                searchProfileImage = v.findViewById(R.id.searchProfileImage);
                searchLineLayout = v.findViewById(R.id.searchLineLayout);

                searchLineLayout.setOnClickListener((a -> {
                    makeFriendRequest(results.results[this.getAdapterPosition()].id);
                }));
            }
        }
    }


    private class RemoveClickLister implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(scheduleInclude.getContext());
            builder.setMessage("Do you want to delete this free interval?");

            builder.setCancelable(true);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                }
            });
            builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Long intervalId = intervalIds.get(((TextView) v).getText());
                    removeScheduleItem(intervalId);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
    }

}
