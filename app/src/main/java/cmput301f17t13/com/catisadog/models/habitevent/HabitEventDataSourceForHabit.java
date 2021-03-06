/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habitevent;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.utils.data.DataSource;

/**
 * Data source for {@link HabitEvent}s associated to a particular habit
 */
public class HabitEventDataSourceForHabit extends DataSource<HabitEvent>
    implements ValueEventListener {

    private static final String TAG = "HabitEventDSForHabit";

    private String userId;
    private String habitKey;

    private ArrayList<HabitEvent> mHabitEventArray;
    private Query habitEventQuery;

    /**
     * Construct the data source
     * @param userId the id of the habit owner
     * @param habitKey the habit key
     */
    public HabitEventDataSourceForHabit(String userId, String habitKey) {
        this.userId = userId;
        this.habitKey = habitKey;
        mHabitEventArray = new ArrayList<>();
    }

    @Override
    public void open() {
        mHabitEventArray.clear();
        habitEventQuery = FirebaseDatabase.getInstance().getReference("events/" + userId)
                .orderByChild("habitKey").equalTo(habitKey);

        habitEventQuery.addValueEventListener(this);
    }

    /**
     * Get the data source reference (for adapters)
     * @return the data source reference
     */
    @Override
    public ArrayList<HabitEvent> getSource() { return mHabitEventArray; }

    /**
     * Aggregate and sort the result in reverse chronological order
     * @param dataSnapshot snapshot of the data
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

        Collections.sort(mHabitEventArray, new Comparator<HabitEvent>() {
            @Override
            public int compare(HabitEvent one, HabitEvent two) {
                return two.getEventDate().compareTo(one.getEventDate().toInstant());
            }
        });

        datasetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e(TAG, databaseError.getDetails());
        close();
    }

    @Override
    public void close() {
        habitEventQuery.removeEventListener(this);
    }
}
