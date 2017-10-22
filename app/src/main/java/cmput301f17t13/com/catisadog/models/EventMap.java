package cmput301f17t13.com.catisadog.models;

import android.location.Location;

public class EventMap {
    private Location centerLocation;
    private int width;
    private int height;

    public EventMap(Location centerLocation, int width, int height) {
        this.centerLocation = centerLocation;
        this.width = width;
        this.height = height;
    }

    public void placeEvent(Location location) {
        // Place marker
    }

}
