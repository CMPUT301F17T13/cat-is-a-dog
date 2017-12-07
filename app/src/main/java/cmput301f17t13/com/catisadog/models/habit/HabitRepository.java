/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habit;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cmput301f17t13.com.catisadog.utils.data.Repository;

/**
 * Class for basic CRUD operations on {@link Habit}s
 */
public class HabitRepository implements Repository<Habit> {

    private DatabaseReference mHabitsRef;

    /**
     * Constructs repository for a particular user
     * @param userId the user id
     */
    public HabitRepository(String userId) {
        this.mHabitsRef = FirebaseDatabase.getInstance().getReference("habits/" + userId);
    }

    /**
     * Add a habit to the database
     * @param habit the habit object
     */
    @Override
    public void add(Habit habit) {
        HabitDataModel habitModel = new HabitDataModel(habit);
        DatabaseReference newRef = mHabitsRef.push();

        habitModel.setKey(newRef.getKey());
        newRef.setValue(habitModel,null);
    }

    /**
     * Update a habit object
     * @param key the habit key
     * @param habit the habit object
     */
    @Override
    public void update(String key, Habit habit) {
        HabitDataModel habitModel = new HabitDataModel(habit);
        mHabitsRef.child(key).getRef().setValue(habitModel, null);
    }

    /**
     * Delete a habit object from the database
     * @param key the habit key
     */
    @Override
    public void delete(String key) {
        mHabitsRef.child(key).removeValue(null);
    }

    /**
     * Get a habit object from the database
     * @param key the habit key
     * @param resultListener result callback
     */
    @Override
    public void get(String key, final OnResultListener<Habit> resultListener) {
        mHabitsRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HabitDataModel model = dataSnapshot.getValue(HabitDataModel.class);

                if(model != null) {
                    resultListener.onResult(model.getHabit());
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
