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

import cmput301f17t13.com.catisadog.utils.data.DataSource;


public class HabitEventDataSourceByComment extends DataSource<HabitEvent>
    implements ValueEventListener {

    private static final String TAG = "HabitEventDSByComment";

    private String userId;
    private String partialComment;
    private ArrayList<HabitEvent> mHabitEventArray;
    private Query habitEventQuery;

    public HabitEventDataSourceByComment(String userId, String partialComment) {
        this.userId = userId;
        this.partialComment = partialComment;

        mHabitEventArray = new ArrayList<>();
    }

    @Override
    public void open() {
        mHabitEventArray.clear();
        habitEventQuery = FirebaseDatabase.getInstance().getReference("events/" + userId)
                .orderByPriority();

        habitEventQuery.addValueEventListener(this);
    }

    /**
     * Get the data source reference (for adapters)
     * @return the data source reference
     */
    @Override
    public ArrayList<HabitEvent> getSource() { return mHabitEventArray; }

    // Habit Event updates

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mHabitEventArray.clear();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            HabitEventDataModel model = snapshot.getValue(HabitEventDataModel.class);

            if (model != null) {
                if (model.getComment().contains(partialComment)) {
                    model.setKey(snapshot.getKey());
                    mHabitEventArray.add(model.getHabitEvent());
                }
            }
        }

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
