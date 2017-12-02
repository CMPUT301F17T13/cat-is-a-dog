package cmput301f17t13.com.catisadog.activities.summary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventRepository;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.utils.IntentConstants;
import cmput301f17t13.com.catisadog.utils.data.Repository;

/**
 * Screen for creating new habit events.
 * Child Activity of HabitSummaryActivity
 *
 * @see HabitSummaryActivity
 * @see HabitEvent
 */
public class AddHabitEventActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        View.OnClickListener {
    private static final String TAG = "AddHabitEventActivity";

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1111;

    private static final int SAVE_BUTTON_ID = 123;

    private Repository<HabitEvent> habitEventRepository;

    private FusedLocationProviderClient mFusedLocationClient;

    private TextView comment;
    private ImageView mapOpacityOverlay;
    private ImageView imageOpacityOverlay;
    private ImageView mapDelete;
    private ImageView imageDelete;
    private ImageView mapAdd;
    private ImageView imageAdd;

    private GoogleMap map;
    private ImageView image;

    // Properties we will add to the HabitEvent object
    private String editingHabitEventKey;
    private String habitKey;
    private Location location;
    private Bitmap imageBitmap;

    /**
     * Add habit event
     * @param savedInstanceState saved Bundle instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        comment = (TextView) findViewById(R.id.editTextComment);
        mapOpacityOverlay = (ImageView) findViewById(R.id.mapOpacityOverlay);
        imageOpacityOverlay = (ImageView) findViewById(R.id.imageOpacityOverlay);
        mapDelete = (ImageView) findViewById(R.id.mapDelete);
        imageDelete = (ImageView) findViewById(R.id.imageDelete);
        mapAdd = (ImageView) findViewById(R.id.mapAdd);
        imageAdd = (ImageView) findViewById(R.id.imageAdd);

        image = (ImageView) findViewById(R.id.image);

        mapOpacityOverlay.setOnClickListener(this);
        imageOpacityOverlay.setOnClickListener(this);
        mapDelete.setOnClickListener(this);
        imageDelete.setOnClickListener(this);

        CurrentUser currentUser = CurrentUser.getInstance();
        if(currentUser != null)
            habitEventRepository = new HabitEventRepository(currentUser.getUserId());

        Bundle b = getIntent().getExtras();
        habitKey = "INVALID KEY"; // or other values
        if(b != null) {
            if(b.containsKey(IntentConstants.ADD_HABIT_EVENT_INTENT_DATA)) {
                setTitle("Add Habit Event");
                habitKey = b.getString(IntentConstants.ADD_HABIT_EVENT_INTENT_DATA);
            } else if(b.containsKey(IntentConstants.EDIT_HABIT_EVENT_INTENT_DATA)) {
                setTitle("Edit Habit Event");
                HabitEvent habitEvent = (HabitEvent)b.getSerializable(IntentConstants.EDIT_HABIT_EVENT_INTENT_DATA);
                restoreHabitEvent(habitEvent);
            }
        }

        updateOverlays();
        Log.d(TAG, "Adding HabitEvent to Habit with key: " + habitKey);
    }

    public void restoreHabitEvent(HabitEvent habitEvent) {
        editingHabitEventKey = habitEvent.getKey();
        habitKey = habitEvent.getHabitKey();

        if(habitEvent.getLatitude() !=0 && habitEvent.getLongitude() !=0) {
            location = new Location("");
            location.setLatitude(habitEvent.getLatitude());
            location.setLongitude(habitEvent.getLongitude());

            //map updated in onMapReady()
        }

        if(habitEvent.getPhotoUrl() != null) {
            byte[] decodedByteArray = Base64.decode(habitEvent.getPhotoUrl(), Base64.NO_WRAP);
            imageBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            image.setImageBitmap(imageBitmap);
        }

        if(habitEvent.getComment() != null && !Objects.equals(habitEvent.getComment(), "")) {
            comment.setText(habitEvent.getComment());
        }
    }

    /**
     * Run this code when the map has been loaded and is ready
     * @param googleMap the map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.setLatLngBoundsForCameraTarget(null);
        googleMap.setMinZoomPreference(6.0f);
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        if(location != null) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions().position(loc)
                    .title("Location"));
            map.moveCamera(CameraUpdateFactory.newLatLng(loc));
        } else {
            LatLng sydney = new LatLng(-33.852, 151.211);
            googleMap.addMarker(new MarkerOptions().position(sydney)
                    .title("Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }

    /**
     * Creates menu items such as "save" button
     * @param menu the menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem menuItem = menu.add(Menu.NONE, SAVE_BUTTON_ID, Menu.NONE, "Save");
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    /**
     * Handler when menu item is selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "item:" + item.getItemId());
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case SAVE_BUTTON_ID:
                saveEvent();
                Toast.makeText(this, "Habit event saved!", Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * OnClick handler for image and map
     * @param v view that was clicked
     */
    public void onClick(View v) {
        if(v == imageOpacityOverlay) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else if(v == mapOpacityOverlay) {
            getLocation();
        } else if(v == imageDelete) {
            Drawable myDrawable = getResources().getDrawable(R.drawable.bird);
            image.setImageDrawable(myDrawable);
            imageBitmap = null;
            updateOverlays();
        } else if(v == mapDelete) {
            location = null;
            updateOverlays();
        }
    }

    /**
     * Request location permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    getLocation();
                } else {
                    // permission denied, boo!
                    Toast.makeText(this, "Do not have permissions to get location!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /**
     * Get user's current location
     */
    private void getLocation() {
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
                            AddHabitEventActivity.this.location = location;
                            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                            map.addMarker(new MarkerOptions().position(loc)
                                    .title("Location"));
                            map.moveCamera(CameraUpdateFactory.newLatLng(loc));
                            updateOverlays();
                        } else {
                            Toast.makeText(AddHabitEventActivity.this, "Error getting location",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Update map and image overlays
     */
    private void updateOverlays() {
        if(imageBitmap != null) {
            imageDelete.setVisibility(View.VISIBLE);
            imageOpacityOverlay.setVisibility(View.INVISIBLE);
            imageAdd.setVisibility(View.INVISIBLE);
        } else {
            imageDelete.setVisibility(View.INVISIBLE);
            imageOpacityOverlay.setVisibility(View.VISIBLE);
            imageAdd.setVisibility(View.VISIBLE);
        }
        if(location != null) {
            mapDelete.setVisibility(View.VISIBLE);
            mapOpacityOverlay.setVisibility(View.INVISIBLE);
            mapAdd.setVisibility(View.INVISIBLE);
        } else {
            mapDelete.setVisibility(View.INVISIBLE);
            mapOpacityOverlay.setVisibility(View.VISIBLE);
            mapAdd.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Activity result handler
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap thumb = ThumbnailUtils.extractThumbnail(photo, 128, 128);
            imageBitmap = thumb;
            image.setImageBitmap(thumb);
            updateOverlays();
        }
    }

    /**
     * Create and store HabitEvent object from filled fields
     */
    private void saveEvent() {
        HabitEvent event = new HabitEvent();

        event.setUserId(CurrentUser.getInstance().getUserId());
        event.setComment(comment.getText().toString());
        event.setEventDate(DateTime.now());
        event.setHabitKey(habitKey);

        if(location != null) {
            event.setLatitude(location.getLatitude());
            event.setLongitude(location.getLongitude());
        }

        if(imageBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            event.setPhotoUrl(encoded);
        }

        if(editingHabitEventKey != null) {
            habitEventRepository.update(editingHabitEventKey, event);
        } else {
            habitEventRepository.add(event);
        }
    }
}
