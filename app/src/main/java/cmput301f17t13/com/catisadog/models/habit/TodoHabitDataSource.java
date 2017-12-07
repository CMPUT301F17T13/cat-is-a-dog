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

/**
 * A data source for habits that are due today
 */
public class TodoHabitDataSource extends DataSource<Habit> {

    public static final String SourceType = "todoDataSource";

    private String userId;

    private HashSet<String> completedToday;
    private ArrayList<Habit> dueHabits;
    private ArrayList<Habit> todoHabits;

    private DueListener dueListener;
    private EventListener eventListener;
    private Query todoQuery;
    private Query eventRef;

    /**
     * Initialize data source for particular user
     * @param userId the user id
     */
    public TodoHabitDataSource(String userId) {
        this.userId = userId;

        todoHabits = new ArrayList<>();
        dueHabits = new ArrayList<>();
        completedToday = new HashSet<>();
    }

    /**
     * Composed of two queries:
     * DueHabits holds habits that are due today
     * completedToday holds ids of habits that have already been completed today
     */
    @Override
    public void open() {
        dueHabits.clear();
        completedToday.clear();

        int dayOfWeek = DateTime.now().getDayOfWeek() - 1;
        todoQuery = FirebaseDatabase.getInstance().getReference("habits/" + userId)
                .orderByChild("schedule/" + Integer.toString(dayOfWeek)).equalTo(true);

        dueListener = new DueListener();
        todoQuery.addValueEventListener(dueListener);

        String nowKey = String.format(Locale.CANADA, "%s_%s", userId, FirebaseUtil.dateToString(DateTime.now()));
        eventRef = FirebaseDatabase.getInstance().getReference("events/" + userId)
                .orderByChild("complete").startAt(nowKey).endAt(FirebaseUtil.terminalKey(nowKey));

        eventListener = new EventListener();
        eventRef.addValueEventListener(eventListener);
    }

    @Override
    public ArrayList<Habit> getSource() {
        return todoHabits;
    }

    /**
     * Listens for habits due today
     */
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

            recreateDataset();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            close();
        }
    }

    /**
     * Listens for events completed today
     */
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

            recreateDataset();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            close();
        }
    }

    /**
     * We merge the due habits and completed today habits accordingly to get the
     * resulting todohabits
     */
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

    /**
     * Notify observers
     */
    private void recreateDataset() {
        merge();
        datasetChanged();
    }

    /**
     * Close the two active listeners
     */
    @Override
    public void close() {
        todoQuery.removeEventListener(dueListener);
        eventRef.removeEventListener(eventListener);
    }
}
