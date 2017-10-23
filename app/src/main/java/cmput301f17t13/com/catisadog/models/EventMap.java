/*
 * Event Map
 *
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models;

import android.location.Location;

import java.util.ArrayList;

/**
 * A collection of HabitEvents on a map
 *
 * @see HabitEvent
 * @see cmput301f17t13.com.catisadog.fragments.history.MapFragment
 */

public class EventMap {
    private Location centerLocation;
    private int width;
    private int height;
    private ArrayList<Location> eventMarkers;

    public EventMap(Location centerLocation, int width, int height) {
        this.centerLocation = centerLocation;
        this.width = width;
        this.height = height;
    }

    /**
     * Place a new marker on the map
     *
     * @param event the new habit event
     */
    public void placeEvent(HabitEvent event) {
        // Place marker
    }

    /**
     * Clear the map of all markers
     */
    public void clear() {

    }
}
