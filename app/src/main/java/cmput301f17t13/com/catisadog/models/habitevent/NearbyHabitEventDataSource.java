package cmput301f17t13.com.catisadog.models.habitevent;

/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

import android.location.Location;
import android.util.Pair;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;

import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.HabitDataModel;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.FirebaseUtil;
import cmput301f17t13.com.catisadog.utils.data.Repository;

public class NearbyHabitEventDataSource extends DataSource<HabitEvent> {

    public static final String SourceType = "recentHabitEventsDataSource";
    private DatabaseReference mGeoFireRef;
    private GeoFire geoFire;

    private ArrayList<HabitEvent> nearbyEvents;

    public NearbyHabitEventDataSource(GeoLocation loc, final ArrayList<String> users) {
        nearbyEvents = new ArrayList<>();
        DatabaseReference geofire_ref = FirebaseDatabase.getInstance().getReference("events_geofire");

        GeoFire geoFire = new GeoFire(geofire_ref);
        GeoQuery geoQuery = geoFire.queryAtLocation(loc, 5);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                String[] parts = key.split("@");
                String userId = parts[0];
                String eventId = parts[1];
                if(users.contains(userId)) {
                    FirebaseDatabase.getInstance().getReference("events/"+userId+"/"+eventId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HabitEventDataModel model = dataSnapshot.getValue(HabitEventDataModel.class);

                            if (model != null) {
                                HabitEvent habitEvent = model.getHabitEvent();
                                nearbyEvents.add(habitEvent);
                                datasetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });
    }

    @Override
    public ArrayList<HabitEvent> getSource() {
        return nearbyEvents;
    }

    @Override
    protected void datasetChanged() {
        setChanged();
        notifyObservers();
    }
}
