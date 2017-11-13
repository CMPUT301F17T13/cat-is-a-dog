/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cmput301f17t13.com.catisadog.utils.data.Repository;


public class HabitDataSource implements Repository<Habit> {

    private DatabaseReference mHabitsRef;

    public HabitDataSource(String userId) {
        mHabitsRef = FirebaseDatabase.getInstance().getReference("habits/" + userId);
    }

    @Override
    public void add(Habit habit) {
        HabitDataModel habitModel = new HabitDataModel(habit);
        mHabitsRef.push().setValue(habitModel);
    }

    @Override
    public void update(String key, Habit habit) {
        HabitDataModel habitModel = new HabitDataModel(habit);
        mHabitsRef.orderByKey().equalTo(key).getRef().setValue(habitModel);
    }

    @Override
    public void delete(String key) {
        mHabitsRef.orderByKey().equalTo(key).getRef().removeValue();
    }
}
