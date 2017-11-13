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
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.utils.IntentConstants;

/**
 * Screen for creating new habit events.
 * Child Activity of HabitSummaryActivity
 *
 * @see HabitSummaryActivity
 * @see cmput301f17t13.com.catisadog.models.HabitEvent
 */
public class AddHabitEventActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        View.OnClickListener {
    private static final String TAG = "AddHabitEventActivity";

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1111;

    private static final int SAVE_BUTTON_ID = 123;

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
    private String habitKey;
    private Location location;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        habitKey = "INVALID KEY"; // or other values
        if(b != null)
            habitKey = b.getString(IntentConstants.ADD_HABIT_EVENT_INTENT_DATA);

        Log.d(TAG, "Adding HabitEvent to Habit with key: " + habitKey);

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
        
        updateOverlays();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.setLatLngBoundsForCameraTarget(null);
        googleMap.setMinZoomPreference(6.0f);
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem menuItem = menu.add(Menu.NONE, SAVE_BUTTON_ID, Menu.NONE, "Save");
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "item:" + item.getItemId());
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case SAVE_BUTTON_ID:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap thumb = ThumbnailUtils.extractThumbnail(photo, 128, 128);
            imageBitmap = thumb;
            image.setImageBitmap(thumb);
            updateOverlays();
        }
    }
}
