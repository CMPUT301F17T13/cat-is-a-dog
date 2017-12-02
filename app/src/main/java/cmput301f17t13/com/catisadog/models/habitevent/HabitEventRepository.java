/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habitevent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cmput301f17t13.com.catisadog.utils.data.Repository;


public class HabitEventRepository implements Repository<HabitEvent> {

    private DatabaseReference mHabitEventsRef;

    public HabitEventRepository(String userId) {
        mHabitEventsRef = FirebaseDatabase.getInstance().getReference("events/" + userId);
    }

    @Override
    public void add(HabitEvent habitEvent) {
        HabitEventDataModel eventModel = new HabitEventDataModel(habitEvent);

        DatabaseReference newEvent = mHabitEventsRef.push();
        eventModel.setKey(newEvent.getKey());

        // Reverse Chronological Order
        newEvent.setPriority(-1 * habitEvent.getEventDate().getMillis(), null);
        newEvent.setValue(eventModel, null);
    }

    @Override
    public void update(String key, HabitEvent habitEvent) {
        HabitEventDataModel eventModel = new HabitEventDataModel(habitEvent);
        mHabitEventsRef.child(key).getRef().setValue(eventModel, null);
    }

    @Override
    public void delete(String key) {
        mHabitEventsRef.child(key).getRef().removeValue(null);
    }

}