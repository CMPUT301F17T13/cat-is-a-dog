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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;

import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataModel;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.FirebaseUtil;

public class TodoHabitDataSource extends DataSource<Habit>
     implements ValueEventListener {

    public static final String SourceType = "todoDataSource";

    private String userId;
    private ArrayList<Habit> todoHabits;

    public TodoHabitDataSource(String userId) {
        this.userId = userId;
        todoHabits = new ArrayList<>();

        int dayOfWeek = DateTime.now().getDayOfWeek() - 1;
        Query todoQuery = FirebaseDatabase.getInstance().getReference("habits/" + userId)
                .orderByChild("schedule/" + Integer.toString(dayOfWeek)).equalTo(true);

        todoQuery.addValueEventListener(this);
    }

    @Override
    public ArrayList<Habit> getSource() {
        return todoHabits;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        DateTime now = DateTime.now();
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference("events/" + userId);

        todoHabits.clear();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            HabitDataModel model = snapshot.getValue(HabitDataModel.class);

            if (model != null) {
                final Habit habit = model.getHabit();

                if (habit.isTodo(now)) {
                    // Check that we haven't done that habit today

                    // Query for all habit events for this habit
                    Query isAlreadyDoneQuery = eventRef.orderByChild("habit")
                            .equalTo(habit.getKey());

                    isAlreadyDoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                String today = FirebaseUtil.dateToString(DateTime.now());

                                for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                                    HabitEventDataModel eventModel = eventSnapshot.getValue(HabitEventDataModel.class);

                                    if (eventModel != null) {
                                        if (today.equals(eventModel.getEventDate())) {
                                            // We have a match, so do not add the habit
                                            return;
                                        }
                                    }
                                }
                            }

                            todoHabits.add(habit);
                            datasetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void datasetChanged() {
        setChanged();
        notifyObservers();
    }
}
