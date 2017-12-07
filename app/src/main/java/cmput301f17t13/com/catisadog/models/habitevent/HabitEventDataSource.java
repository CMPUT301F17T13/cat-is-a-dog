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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 * Queries {@link HabitEvent}s owned by a user, sorted in reverse chronological order
 */
public class HabitEventDataSource extends DataSource<HabitEvent>
        implements ValueEventListener {

    private static final String TAG = "HabitEventDataSource";

    private String userId;
    private ArrayList<HabitEvent> mHabitEventArray;
    private Query habitEventQuery;

    /**
     * Constructs the data source
     * @param userId id of the user to query
     */
    public HabitEventDataSource(String userId) {
        this.userId = userId;
        mHabitEventArray = new ArrayList<>();
    }

    /**
     * Open the query
     */
    @Override
    public void open() {
        mHabitEventArray.clear();
        habitEventQuery = FirebaseDatabase.getInstance()
                .getReference("events/" + userId).orderByChild("eventDate");

        habitEventQuery.addValueEventListener(this);
    }

    /**
     * Get the data source reference (for adapters)
     * @return the data source reference
     */
    @Override
    public ArrayList<HabitEvent> getSource() { return mHabitEventArray; }

    // Habit Event updates

    /**
     * Aggregates and sorts the query result
     * @param dataSnapshot the root snapshot of the query
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mHabitEventArray.clear();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            HabitEventDataModel model = snapshot.getValue(HabitEventDataModel.class);

            if (model != null) {
                model.setKey(snapshot.getKey());
                mHabitEventArray.add(model.getHabitEvent());
            }
        }

        Collections.reverse(mHabitEventArray);
        datasetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e(TAG, databaseError.getDetails());
        close();
    }

    /**
     * Remove the query listener
     */
    @Override
    public void close() {
        habitEventQuery.removeEventListener(this);
    }
}
