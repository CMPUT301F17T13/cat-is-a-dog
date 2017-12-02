package cmput301f17t13.com.catisadog.activities.history;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataSource;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.utils.data.DataSource;

public class HabitHistoryActivity extends AppCompatActivity implements
        Observer,
        OnMapReadyCallback {

    private GoogleMap map;
    private ListView habitsListView;
    private HabitHistoryActivity.HabitHistoryAdapter habitsAdapter;

    public DataSource<HabitEvent> eventDataSource;
    public ArrayList<HabitEvent> habitEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);

        String userId = CurrentUser.getInstance().getUserId();
        eventDataSource = new HabitEventDataSource(userId);
        habitEvents = eventDataSource.getSource();
        eventDataSource.addObserver(this);

        habitsListView = (ListView) findViewById(R.id.list);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        LatLngBounds bounds = boundsBuilder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        map.moveCamera(cu);
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
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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

            /*View.OnClickListener addHabitEventButtonListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Event", "Add habit event");
                    Intent intent = new Intent(getActivity(), AddHabitEventActivity.class);
                    Bundle b = new Bundle();
                    b.putString(IntentConstants.ADD_HABIT_EVENT_INTENT_DATA, habit.getKey());
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            };

            addHabitEventButton.setOnClickListener(addHabitEventButtonListener);*/

            return convertView;
        }
    }
}
