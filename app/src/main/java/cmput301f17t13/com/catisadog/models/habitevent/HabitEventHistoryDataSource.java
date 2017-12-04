package cmput301f17t13.com.catisadog.models.habitevent;

/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

import android.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.HabitDataModel;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.FirebaseUtil;

public class HabitEventHistoryDataSource extends DataSource<Pair<Habit, HabitEvent>> {

    public static final String SourceType = "todoDataSource";

    private String userId;

//    private HashSet<String> completedToday;
    private HashMap<String, Habit> habitsById;
    private ArrayList<HabitEvent> recentEvents;
    private ArrayList<Pair<Habit, HabitEvent>> habitHabitEventPairs;

    public HabitEventHistoryDataSource(String userId) {
        this.userId = userId;

        recentEvents = new ArrayList<>();
        habitsById = new HashMap<>();

        Query habitsQuery = FirebaseDatabase.getInstance().getReference("habits/" + userId);
        habitsQuery.addValueEventListener(new cmput301f17t13.com.catisadog.models.habitevent.HabitEventHistoryDataSource.HabitsListener());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        long millis = cal.getTimeInMillis();
        Query eventsQuery = FirebaseDatabase.getInstance().getReference("events/" + userId)
                .orderByChild("eventDate").startAt(millis).endAt(DateTime.now().getMillis());
        eventsQuery.addValueEventListener(new cmput301f17t13.com.catisadog.models.habitevent.HabitEventHistoryDataSource.EventListener());

    }

    @Override
    public ArrayList<Pair<Habit, HabitEvent>> getSource() {
        return habitHabitEventPairs;
    }

    private class HabitsListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            DateTime now = DateTime.now();

            habitsById.clear();

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                HabitDataModel model = snapshot.getValue(HabitDataModel.class);

                if (model != null) {
                    Habit habit = model.getHabit();
                    if (habit != null) {
                        habitsById.put(habit.getKey(), habit);
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
