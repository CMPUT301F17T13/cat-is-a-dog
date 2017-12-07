package cmput301f17t13.com.catisadog.activities.history;

import android.Manifest;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.AddHabitEventActivity;
import cmput301f17t13.com.catisadog.activities.BaseDrawerActivity;
import cmput301f17t13.com.catisadog.fragments.history.FilterDialogFragment;
import cmput301f17t13.com.catisadog.fragments.history.FilterDialogFragment.FilterDialogResultListener;
import cmput301f17t13.com.catisadog.models.followrequest.SocialDataSource;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.HabitDataSource;
import cmput301f17t13.com.catisadog.models.habit.HabitRepository;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataSource;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataSourceByComment;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataSourceForHabit;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventRepository;
import cmput301f17t13.com.catisadog.models.habitevent.NearbyHabitEventDataSource;
import cmput301f17t13.com.catisadog.models.habitevent.RecentHabitEventHistoryDataSource;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.models.user.User;
import cmput301f17t13.com.catisadog.models.user.UserRepository;
import cmput301f17t13.com.catisadog.utils.IntentConstants;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.Repository;

import static cmput301f17t13.com.catisadog.fragments.history.FilterDialogFragment.FilterType.MY_RECENT_EVENTS;

public class HabitHistoryActivity extends BaseDrawerActivity implements
        Observer, OnMapReadyCallback, FilterDialogResultListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1111;
    private static final String BUNDLE_FILTER_TYPE = "filterType";
    private static final String BUNDLE_FILTER_DATA = "filterData";

    private GoogleMap map;
    private ListView habitsListView;
    private HabitHistoryAdapter habitHistoryAdapter;

    private FusedLocationProviderClient mFusedLocationClient;

    private DataSource<Habit> habitDataSource;
    private DataSource<HabitEvent> eventDataSource;
    private DataSource<User> followingDataSource;
    private Repository<HabitEvent> habitEventRepository;
    private TreeMap<String, Habit> habitKeyHabitMap = new TreeMap<>();

    private Location location;

    private ArrayList<Habit> habits;
    private ArrayList<HabitEvent> habitEvents;
    private ArrayList<User> following;
    private ListUpdater listUpdater;

    private FilterDialogFragment.FilterType filterType;
    private String filterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);
        drawToolbar();

        listUpdater = new ListUpdater();

        String userId = CurrentUser.getInstance().getUserId();
        habitEventRepository = new HabitEventRepository(userId);

        habitDataSource = new HabitDataSource(userId);
        habits = habitDataSource.getSource();
        habitDataSource.addObserver(listUpdater);

        followingDataSource = new SocialDataSource(userId, SocialDataSource.UserType.FOLLOWING);
        following = followingDataSource.getSource();
        followingDataSource.addObserver(listUpdater);

        updateHabitKeyHabitMap();

        habitsListView = (ListView) findViewById(R.id.list);

        if(filterType == null) {
            filterType = MY_RECENT_EVENTS;
        }
        filterResult(filterType, filterData);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        final FilterDialogResultListener resultListener = this;
        final ImageButton filterButton =
                (ImageButton) findViewById(R.id.searchButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle habitsBundle = new Bundle();
                habitsBundle.putSerializable("habits", habits);
                FilterDialogFragment filterDialog = new FilterDialogFragment();
                filterDialog.setArguments(habitsBundle);
                filterDialog.setResultListener(resultListener);
                filterDialog.show(getFragmentManager(), "filter");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_FILTER_TYPE, filterType);
        outState.putString(BUNDLE_FILTER_DATA, filterData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        filterType = (FilterDialogFragment.FilterType) savedInstanceState.getSerializable(BUNDLE_FILTER_TYPE);
        filterData = savedInstanceState.getString(BUNDLE_FILTER_DATA);

        if(filterType == null) {
            filterType = MY_RECENT_EVENTS;
        }
        filterResult(filterType, filterData);
    }

    @Override
    public void onStart() {
        super.onStart();
        habitHistoryAdapter = new HabitHistoryAdapter(this, habitEvents);
        habitsListView.setAdapter(habitHistoryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        habitDataSource.open();
        eventDataSource.open();
        followingDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        habitDataSource.close();
        eventDataSource.close();
        followingDataSource.close();
    }

    /**
     * Run this code when the map has been loaded and is ready
     * @param googleMap the map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setLatLngBoundsForCameraTarget(null);
        map.setMinZoomPreference(3.0f);
        map.setMaxZoomPreference(17.0f);
    }

    public void updateListView() {
        if (habitHistoryAdapter != null) {
            habitHistoryAdapter.notifyDataSetChanged();
            updateHabitKeyHabitMap();
        }
    }

    @Override
    public void update(final Observable observable, final Object o) {
        updateListView();

        if(map == null) {
            final Handler delayHandler = new Handler();
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    update(observable, o);
                }
            }, 100);
            return;
        }

        map.clear();
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        int n = 0;

        for(int i = 0; i < habitEvents.size(); ++i) {
            HabitEvent event = habitEvents.get(i);
            double lat = event.getLatitude();
            double lng = event.getLongitude();
            if(lat == 0 || lng == 0) continue;
            n++;

            LatLng pos = new LatLng(lat, lng);
            Marker marker = map.addMarker(new MarkerOptions()
                .position(pos)
                .title("Location")
                .snippet(""+i));
            boundsBuilder.include(marker.getPosition());
        }
        
        if(n > 0) {
            LatLngBounds bounds = boundsBuilder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            map.moveCamera(cu);
        }
    }

    @Override
    public void filterResult(FilterDialogFragment.FilterType filterType,
                             String filterData) {
        this.filterType = filterType;
        this.filterData = filterData;
        if (eventDataSource != null) {
            eventDataSource.deleteObserver(this);
            eventDataSource.close();
        }

        String userId = CurrentUser.getInstance().getUserId();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        switch(filterType) {
            case NEAR_LOCATION:
                ArrayList<String> idList = new ArrayList<>();
                for(User followingUser : following) {
                    idList.add(followingUser.getUserId());
                }
                idList.add(userId);
                filterByNearby(idList);
                actionBar.setSubtitle("Filter: Within 5 km");
                return;
            case MY_RECENT_EVENTS:
                actionBar.setSubtitle("Filter: My recent events");
                eventDataSource = new HabitEventDataSource(userId);
                break;
            case FRIENDS_RECENT_EVENTS:
                actionBar.setSubtitle("Filter: Friends' recent events");
                ArrayList<String> followingIds = new ArrayList<>();
                for(User followingUser : following) {
                    followingIds.add(followingUser.getUserId());
                }

                eventDataSource = new RecentHabitEventHistoryDataSource(followingIds);
                break;
            case SEARCH_BY_HABIT:
                actionBar.setSubtitle("Filter: habit");
                eventDataSource = new HabitEventDataSourceForHabit(userId, filterData);
                break;
            case SEARCH_BY_COMMENT:
                actionBar.setSubtitle("Filter by Comment: '" + filterData + '\'');
                eventDataSource = new HabitEventDataSourceByComment(userId, filterData);
                break;
        }

        habitEvents = eventDataSource.getSource();
        eventDataSource.addObserver(this);
        eventDataSource.open();
        habitHistoryAdapter = new HabitHistoryAdapter(this, habitEvents);
        habitsListView.setAdapter(habitHistoryAdapter);
    }

    private class ListUpdater implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            updateListView();
        }
    }

    /**
     * An adapter for converting habit objects into to-do habitEvents to be displayed in a list
     * view.
     */
    private class HabitHistoryAdapter extends ArrayAdapter<HabitEvent> {
        public HabitHistoryAdapter(Context context, ArrayList<HabitEvent> habitEvents) {
            super(context, 0, habitEvents);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            final HabitEvent habitEvent = this.getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_my_habit_events, parent, false);
            }

            final TextView titleView = (TextView) convertView.findViewById(R.id.myHabitEventListItemTitle);
            final TextView reasonView = (TextView) convertView.findViewById(R.id.myHabitEventListItemReason);
            TextView startDateView = (TextView) convertView.findViewById(R.id.myHabitEventListItemStartDate);
            HabitRepository hr = new HabitRepository(habitEvent.getUserId());
            hr.get(habitEvent.getHabitKey(), new Repository.OnResultListener<Habit>() {
                @Override
                public void onResult(Habit habit) {
                    titleView.setText(habit.getTitle());
                    if(!Objects.equals(habit.getUserId(), CurrentUser.getInstance().getUserId())) {
                        UserRepository ur = new UserRepository();
                        ur.get(habit.getUserId(), new Repository.OnResultListener<User>() {
                            @Override
                            public void onResult(User user) {
                                if(habitEvent.getComment() != null && habitEvent.getComment().length() > 0)
                                    reasonView.setText(user.getDisplayName() + " - " + habitEvent.getComment());
                                else
                                    reasonView.setText(user.getDisplayName());
                            }
                        });
                    } else {
                        reasonView.setText(habitEvent.getComment());
                    }
                }
            });
            startDateView.setText(habitEvent.getEventDate().toString("d MMM"));

            View.OnClickListener addHabitEventButtonListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Event", "Edit habit event");
                    Intent intent = new Intent(parent.getContext(), AddHabitEventActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(IntentConstants.EDIT_HABIT_EVENT_INTENT_DATA, habitEvent);
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            };

            View.OnLongClickListener deleteHabitEventButtonListener = new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    Log.d("Event", "Delete habit event");

                    AlertDialog.Builder alert = new AlertDialog.Builder(parent.getContext());
                    alert.setTitle("Confirmation");
                    alert.setMessage("Are you sure you would like to delete the habit event?");

                    alert.setPositiveButton("Delete", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            habitEventRepository.delete(habitEvent.getKey());
                            dialog.dismiss();
                        }
                    });
                    alert.setNegativeButton("Cancel", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                    return true;
                }
            };

            convertView.setOnClickListener(addHabitEventButtonListener);
            convertView.setOnLongClickListener(deleteHabitEventButtonListener);

            return convertView;
        }
    }

    /**
     * Filter by within 5km
     */
    private void filterByNearby(final ArrayList<String> followingIds) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.

                        if (location != null) {
                            // Logic to handle location object
                            HabitHistoryActivity.this.location = location;
                            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

                            eventDataSource = new NearbyHabitEventDataSource(new GeoLocation(loc.latitude, loc.longitude), followingIds);
                            eventDataSource.addObserver(HabitHistoryActivity.this);
                            eventDataSource.open();
                            habitEvents = eventDataSource.getSource();
                            habitHistoryAdapter = new HabitHistoryAdapter(HabitHistoryActivity.this, habitEvents);
                            habitsListView.setAdapter(habitHistoryAdapter);

                            // TODO: update the data source here with the list of friends
                        } else {
                            Toast.makeText(HabitHistoryActivity.this, "Error getting location",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void updateHabitKeyHabitMap() {
        habitKeyHabitMap.clear();
        for (Habit habit : habits) {
            habitKeyHabitMap.put(habit.getKey(), habit);
        }
    }
}
