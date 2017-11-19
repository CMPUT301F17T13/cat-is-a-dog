/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habitevent;

import android.util.Log;

import cmput301f17t13.com.catisadog.models.habit.HabitDataModel;
import cmput301f17t13.com.catisadog.utils.data.DataSource;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class HabitEventDataSource extends DataSource<HabitEvent> implements
        ChildEventListener {

    private static final String TAG = "HabitDataSource";

    private DatabaseReference mHabitEventsRef;

    private LinkedHashMap<String, HabitEvent> mHabitEvents;
    private ArrayList<HabitEvent> mHabitEventArray;

    public HabitEventDataSource(String userId) {
        mHabitEventsRef = FirebaseDatabase.getInstance().getReference("events/" + userId);
        mHabitEventsRef.addChildEventListener(this);

        mHabitEvents = new LinkedHashMap<>();
        mHabitEventArray = new ArrayList<>();
    }

    /**
     * Get the data source reference (for adapters)
     * @return the data source reference
     */
    @Override
    public ArrayList<HabitEvent> getSource() { return mHabitEventArray; }

    // Habit updates

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
        HabitEventDataModel model = dataSnapshot.getValue(HabitEventDataModel.class);

        /*if (model != null) {
            model.setKey(dataSnapshot.getKey());
            mHabitEvents.put(dataSnapshot.getKey(), model.getHabitEvent());
            datasetChanged();
        }*/
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
        HabitDataModel model = dataSnapshot.getValue(HabitDataModel.class);

        /*if (model != null) {
            model.setKey(dataSnapshot.getKey());
            mHabitEvents.put(dataSnapshot.getKey(), model.getHabit());
            datasetChanged();
        }*/
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

        mHabitEvents.remove(dataSnapshot.getKey());
        datasetChanged();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
        // Do nothing, doesn't affect ordering
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w(TAG, "loadHabits:onCancelled", databaseError.toException());
    }

    private void datasetChanged() {
        mHabitEventArray.clear();
        mHabitEventArray.addAll(mHabitEvents.values());
        setChanged();
        notifyObservers();
    }
}
