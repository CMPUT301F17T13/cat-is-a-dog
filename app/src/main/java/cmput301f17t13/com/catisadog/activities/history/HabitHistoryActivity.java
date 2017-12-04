package cmput301f17t13.com.catisadog.activities.history;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.AddHabitEventActivity;
import cmput301f17t13.com.catisadog.activities.BaseDrawerActivity;
import cmput301f17t13.com.catisadog.fragments.history.FilterDialogFragment;
import cmput301f17t13.com.catisadog.fragments.history.FilterDialogFragment.FilterDialogResultListener;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.HabitDataSource;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataSource;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventRepository;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.utils.IntentConstants;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.Repository;

public class HabitHistoryActivity extends BaseDrawerActivity implements
        Observer, OnMapReadyCallback, FilterDialogResultListener {

    private GoogleMap map;
    private ListView habitsListView;
    private HabitHistoryActivity.HabitHistoryAdapter habitsAdapter;

    public DataSource<Habit> habitDataSource;
    public DataSource<HabitEvent> eventDataSource;
    public ArrayList<Habit> habits;
    public ArrayList<HabitEvent> habitEvents;
    private Repository<HabitEvent> habitEventRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);
        drawToolbar();

        CurrentUser currentUser = CurrentUser.getInstance();
        String userId = currentUser.getUserId();
        eventDataSource = new HabitEventDataSource(userId);
        habitEvents = eventDataSource.getSource();
        eventDataSource.addObserver(this);
        habitEventRepository = new HabitEventRepository(userId);

        habitDataSource = new HabitDataSource(userId);
        habits = habitDataSource.getSource();
        habitDataSource.addObserver(this);

        habitsListView = (ListView) findViewById(R.id.list);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
    public void onStart() {
        super.onStart();
        habitsAdapter = new HabitHistoryActivity.HabitHistoryAdapter(this, habitEvents);
        habitsListView.setAdapter(habitsAdapter);
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
        if (habitsAdapter != null) {
            habitsAdapter.notifyDataSetChanged();
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
            }, 1000);
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
        int i = 0;
    }

    /**
     * An adapter for converting habit objects into to-do habitEvents to be displayed in a list
     * view.
     */
    private class HabitHistoryAdapter extends ArrayAdapter<HabitEvent> {
        public HabitHistoryAdapter(Context context, ArrayList<HabitEvent> habits) {
            super(context, 0, habits);
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
            titleView.setText("Habit " + habitEvent.getHabitKey() + " #"+(position+1));
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
}
