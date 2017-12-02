/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habit;


import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;

import cmput301f17t13.com.catisadog.utils.data.DataSource;

public class TodoHabitDataSource extends HabitDataSource {
    public static final String SourceType = "todoDataSource";

    public TodoHabitDataSource(String userId) {
        super(userId);
    }

    /*@Override
    protected void setReference() {
        DateTime now = DateTime.now();
        int dayOfWeek = now.dayOfWeek().get() - 1;

        mHabitsRef = FirebaseDatabase.getInstance().getReference("habitEvents/" + userId)
                .orderByChild("startDate").endAt(now.getMillis())
                .orderByChild("schedule/" + Integer.toString(dayOfWeek)).equalTo(true).getRef();
    }*/

    @Override
    protected boolean isValid(Habit habit) {
        return habit.isTodo(DateTime.now());
    }

    @Override
    public String getType() {
        return SourceType;
    }
}
