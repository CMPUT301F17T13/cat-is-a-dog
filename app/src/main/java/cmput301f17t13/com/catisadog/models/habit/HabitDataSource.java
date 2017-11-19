/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habit;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cmput301f17t13.com.catisadog.utils.data.DataSource;

/**
 * Firebase datasource implementation for a user's habits
 */
public class HabitDataSource extends DataSource<Habit> {

    private static final String TAG = "HabitDataSource";

    DatabaseReference mHabitsRef;
    String userId;

    private LinkedHashMap<String, Habit> mHabits;
    private ArrayList<Habit> mHabitArray;

    public HabitDataSource(String userId) {
        this.userId = userId;

        mHabits = new LinkedHashMap<>();
        mHabitArray = new ArrayList<>();

        setReference();
    }

    protected void setReference() {
        mHabitsRef = FirebaseDatabase.getInstance().getReference("habits/" + userId);
        mHabitsRef.addChildEventListener(this);
    }

    protected boolean isValid(Habit habit) {
        return true;
    }

    public String getType() {
        return "habitDataSource";
    }

    /**
     * Get the data source reference (for adapters)
     * @return the data source reference
     */
    @Override
    public ArrayList<Habit> getSource() { return mHabitArray; }

    // Habit updates

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
        HabitDataModel model = dataSnapshot.getValue(HabitDataModel.class);

        if (model != null) {
            model.setKey(dataSnapshot.getKey());
            Habit habit = model.getHabit();
            if (isValid(habit)) {
                mHabits.put(dataSnapshot.getKey(), model.getHabit());
                datasetChanged();
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
        HabitDataModel model = dataSnapshot.getValue(HabitDataModel.class);

        if (model != null) {
            model.setKey(dataSnapshot.getKey());
            Habit habit = model.getHabit();
            if (isValid(habit)) {
                mHabits.put(dataSnapshot.getKey(), model.getHabit());
                datasetChanged();
            }
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

        mHabits.remove(dataSnapshot.getKey());
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
        mHabitArray.clear();
        mHabitArray.addAll(mHabits.values());
        setChanged();
        notifyObservers(getType());
    }
}
