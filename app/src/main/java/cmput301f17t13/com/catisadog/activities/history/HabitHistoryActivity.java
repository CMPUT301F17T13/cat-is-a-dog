package cmput301f17t13.com.catisadog.activities.history;

import android.Manifest;
import android.app.ActionBar;
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
import java.util.List;
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
import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataSource;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataSourceByComment;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataSourceForHabit;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventRepository;
import cmput301f17t13.com.catisadog.models.habitevent.NearbyHabitEventDataSource;
import cmput301f17t13.com.catisadog.models.habitevent.RecentHabitEventHistoryDataSource;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.models.user.User;
import cmput301f17t13.com.catisadog.utils.IntentConstants;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.Repository;

import static cmput301f17t13.com.catisadog.fragments.history.FilterDialogFragment.FilterType.MY_RECENT_EVENTS;

public class HabitHistoryActivity extends BaseDrawerActivity implements
        Observer, OnMapReadyCallback, FilterDialogResultListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1111;

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
        filterResult(MY_RECENT_EVENTS, null);

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

//        getActionBar().setSubtitle("Filter: My recent events");
    }

    @Override
    public void onStart() {
        super.onStart();
        habitHistoryAdapter = new HabitHistoryAdapter(this, habitEvents);
        habitsListView.setAdapter(habitHistoryAdapter);
    }

    /**
     * Run this code when the map has been loaded and is ready
     * @param googleMap the map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setLatLngBoundsForCameraTarget(null);
        map.setMinZoomPreference(6.0f);
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
        for(int i = 0; i < habitEvents.size(); ++i) {
            HabitEvent event = habitEvents.get(i);
            double lat = event.getLatitude();
            double lng = event.getLongitude();
            LatLng pos = new LatLng(lat, lng);
            Marker marker = map.addMarker(new MarkerOptions()
                .position(pos)
                .title("Location")
                .snippet(""+i));
            boundsBuilder.include(marker.getPosition());
        }
        
        if(habitEvents.size() > 0) {
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
        if (eventDataSource != null) {
            eventDataSource.deleteObserver(this);
        }

        String userId = CurrentUser.getInstance().getUserId();

        switch(filterType) {
            case NEAR_LOCATION:
                ArrayList<String> idList = new ArrayList<>();
                for(User followingUser : following) {
                    idList.add(followingUser.getUserId());
                }
                idList.add(userId);
                filterByNearby(idList);
                return;
            case MY_RECENT_EVENTS:
                eventDataSource = new HabitEventDataSource(userId);
                break;
            case FRIENDS_RECENT_EVENTS:
                ArrayList<String> followingIds = new ArrayList<>();
                for(User followingUser : following) {
                    followingIds.add(followingUser.getUserId());
                }

                eventDataSource = new RecentHabitEventHistoryDataSource(followingIds);
                break;
            case SEARCH_BY_HABIT:
                eventDataSource = new HabitEventDataSourceForHabit(userId, filterData);
                break;
            case SEARCH_BY_COMMENT:
                eventDataSource = new HabitEventDataSourceByComment(userId, filterData);
                break;
        }

        habitEvents = eventDataSource.getSource();
        eventDataSource.addObserver(this);
        habitHistoryAdapter = new HabitHistoryAdapter(this, habitEvents);
        habitsListView.setAdapter(habitHistoryAdapter);
//        ActionBar actionBar = getActionBar();
//        switch (filterType) {
//            case nearLocation:
//                actionBar.setSubtitle("Filter: Within 5 km");
//                break;
//            case searchByHabit:
//                String title = habitKeyHabitMap.get(filterData).getTitle();
//                actionBar.setSubtitle("Filter: '" + title + "' habit");
//                break;
//            case searchByComment:
//                actionBar.setSubtitle("Filter: '" + filterData + '\'');
//                break;
//            case myRecentEvents:
//                actionBar.setSubtitle("Filter: My recent events");
//                break;
//            case friendsRecentEvents:
//                actionBar.setSubtitle("Filter: Friends' recent events");
//                break;
//        }
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

            TextView titleView = (TextView) convertView.findViewById(R.id.myHabitEventListItemTitle);
            TextView reasonView = (TextView) convertView.findViewById(R.id.myHabitEventListItemReason);
            TextView startDateView = (TextView) convertView.findViewById(R.id.myHabitEventListItemStartDate);

            String habitTitle;
            try {
                habitTitle = habitKeyHabitMap.get(habitEvent.getHabitKey())
                        .getTitle();
            } catch (Exception e) {
                habitTitle = "Habit";
            }

            titleView.setText(habitTitle);
            reasonView.setText(habitEvent.getComment());
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

                    alert.setPositiveButton("Delete habit event", new Dialog.OnClickListener() {
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
