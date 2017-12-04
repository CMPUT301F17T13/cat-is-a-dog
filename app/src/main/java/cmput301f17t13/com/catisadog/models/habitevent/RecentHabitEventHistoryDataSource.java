package cmput301f17t13.com.catisadog.models.habitevent;

/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

import android.util.Pair;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.HabitDataModel;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.FirebaseUtil;

public class RecentHabitEventHistoryDataSource extends DataSource<HabitEvent>
    implements ChildEventListener {

    public static final String SourceType = "RecentHabitEventsDataSource";

    private TreeMap<Long, HabitEvent> recentHabitEventMap;
    private ArrayList<HabitEvent> recentEvents;

    public RecentHabitEventHistoryDataSource(List<String> idList) {
        recentEvents = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        long millis = cal.getTimeInMillis();

        for (String id : idList) {
            Query eventsQuery = FirebaseDatabase.getInstance().getReference("events/" + id)
                    .orderByChild("eventDate").startAt(millis).endAt(DateTime.now().getMillis());
            eventsQuery.addChildEventListener(this);
        }
    }

    @Override
    public ArrayList<HabitEvent> getSource() {
        return recentEvents;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        HabitEventDataModel model = dataSnapshot.getValue(HabitEventDataModel.class);

        if (model != null) {
            HabitEvent habitEvent = model.getHabitEvent();
            recentHabitEventMap.put((Long) dataSnapshot.getPriority(), habitEvent);
        }

        datasetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        HabitEventDataModel model = dataSnapshot.getValue(HabitEventDataModel.class);

        if (model != null) {
            HabitEvent habitEvent = model.getHabitEvent();
            recentHabitEventMap.put((Long) dataSnapshot.getPriority(), habitEvent);
        }

        datasetChanged();
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        recentHabitEventMap.remove((Long) dataSnapshot.getPriority());
        datasetChanged();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

    @Override
    public void onCancelled(DatabaseError databaseError) { }

    @Override
    protected void datasetChanged() {
        recentEvents.clear();
        recentEvents.addAll(recentHabitEventMap.values());

        setChanged();
        notifyObservers();
    }
}