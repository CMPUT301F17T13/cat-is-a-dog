/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habitevent;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cmput301f17t13.com.catisadog.utils.data.Repository;


public class HabitEventRepository implements Repository<HabitEvent> {

    private DatabaseReference mHabitEventsRef;
    private DatabaseReference mGeoFireRef;
    GeoFire geoFire;
    private String userId;

    public HabitEventRepository(String userId) {
        mHabitEventsRef = FirebaseDatabase.getInstance().getReference("events/" + userId);
        this.userId = userId;

        mGeoFireRef = FirebaseDatabase.getInstance().getReference("events_geofire/");
        geoFire = new GeoFire(mGeoFireRef);
    }

    @Override
    public void add(HabitEvent habitEvent) {
        HabitEventDataModel eventModel = new HabitEventDataModel(habitEvent);

        DatabaseReference newEvent = mHabitEventsRef.push();
        eventModel.setKey(newEvent.getKey());

        // Reverse Chronological Order
        newEvent.setValue(eventModel, -1 * habitEvent.getEventDate().getMillis(), null);
        geoFire.setLocation(userId+'@'+newEvent.getKey(), new GeoLocation(habitEvent.getLatitude(), habitEvent.getLongitude()));
    }

    @Override
    public void update(String key, HabitEvent habitEvent) {
        HabitEventDataModel eventModel = new HabitEventDataModel(habitEvent);
        mHabitEventsRef.child(key).getRef().setValue(eventModel, null);
        geoFire.setLocation(userId+'@'+habitEvent.getKey(), new GeoLocation(habitEvent.getLatitude(), habitEvent.getLongitude()));
    }

    @Override
    public void delete(String key) {
        mHabitEventsRef.child(key).getRef().removeValue(null);
        geoFire.removeLocation(userId+'@'+key);
    }

    @Override
    public void get(String key, final OnResultListener<HabitEvent> resultListener) {
        mHabitEventsRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HabitEventDataModel model = dataSnapshot.getValue(HabitEventDataModel.class);

                if(model != null) {
                    resultListener.onResult(model.getHabitEvent());
                }
                else {
                    resultListener.onResult(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
