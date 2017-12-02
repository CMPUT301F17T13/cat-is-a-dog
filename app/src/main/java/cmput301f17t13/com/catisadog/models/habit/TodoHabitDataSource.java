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
import java.util.HashSet;
import java.util.Locale;

import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataModel;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.FirebaseUtil;

public class TodoHabitDataSource extends DataSource<Habit> {

    public static final String SourceType = "todoDataSource";

    private String userId;

    private HashSet<String> completedToday;
    private ArrayList<Habit> dueHabits;
    private ArrayList<Habit> todoHabits;

    public TodoHabitDataSource(String userId) {
        this.userId = userId;

        todoHabits = new ArrayList<>();
        dueHabits = new ArrayList<>();
        completedToday = new HashSet<>();

        int dayOfWeek = DateTime.now().getDayOfWeek() - 1;
        Query todoQuery = FirebaseDatabase.getInstance().getReference("habits/" + userId)
                .orderByChild("schedule/" + Integer.toString(dayOfWeek)).equalTo(true);

        todoQuery.addValueEventListener(new DueListener());

        String nowKey = String.format(Locale.CANADA, "%s_%s", userId, FirebaseUtil.dateToString(DateTime.now()));
        Query eventRef = FirebaseDatabase.getInstance().getReference("events/" + userId)
                .orderByChild("complete").startAt(nowKey).endAt(nowKey+"\uf8ff");

        eventRef.addValueEventListener(new EventListener());
    }

    @Override
    public ArrayList<Habit> getSource() {
        return todoHabits;
    }

    private class DueListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            DateTime now = DateTime.now();

            dueHabits.clear();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                HabitDataModel model = snapshot.getValue(HabitDataModel.class);

                if (model != null) {
                    Habit habit = model.getHabit();
                    if (habit != null) {
                        dueHabits.add(habit);
                    }
                }
            }

            datasetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private class EventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            completedToday.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                HabitEventDataModel model = snapshot.getValue(HabitEventDataModel.class);

                if (model != null) {
                    HabitEvent habitEvent = model.getHabitEvent();
                    if (habitEvent != null) {
                        completedToday.add(habitEvent.getHabitKey());
                    }
                }
            }

            datasetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private void merge() {
        todoHabits.clear();
        for(Habit h : dueHabits) {
            if (h.isTodo(DateTime.now())) {
                if (completedToday.contains(h.getKey())) {
                    h.setComplete();
                }
                todoHabits.add(h);
            }
        }
    }

    private void datasetChanged() {
        merge();
        setChanged();
        notifyObservers();
    }
}
