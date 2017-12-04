/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habit;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEventDataModel;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.date.DateUtil;
import cmput301f17t13.com.catisadog.utils.date.Week;

public class CompletionMetricDataSource extends DataSource<Double>
        implements ValueEventListener {

    private Habit habit;
    private double[] completionRates;
    private ArrayList<Double> completionResultArray;
    private CountDownLatch completionLatch;

    public CompletionMetricDataSource(Habit habit) {
        this.habit = habit;
        completionRates = new double[7];
        completionLatch = new CountDownLatch(7);
        completionResultArray = new ArrayList<>();

        if(habit.getSchedule().size() > 0) {
            for(Week week : DateUtil.GetNPastWeeks(DateTime.now(), 7)) {
                String startKey = HabitEventDataModel.habitStampKey(habit.getKey(), week.getStartOfWeek());
                String endKey = HabitEventDataModel.habitStampKey(habit.getKey(), week.getEndOfWeek());

                Query eventCountQuery = FirebaseDatabase.getInstance().getReference("events/" + habit.getUserId())
                        .orderByChild("habitStamp").startAt(startKey).endAt(endKey);

                eventCountQuery.addListenerForSingleValueEvent(this);
            }
        }
    }

    @Override
    public ArrayList<Double> getSource() {
        return completionResultArray;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        long completions = dataSnapshot.getChildrenCount();

        if(completions > 0) {
            int dueCount = habit.getSchedule().size();

            Long eventTimestamp =
                    (Long) dataSnapshot.getChildren().iterator().next().child("eventDate").getValue();

            int weeksAgo = DateUtil.WeekDifference(new DateTime(eventTimestamp), DateTime.now());

            if (weeksAgo == 0) {
                int neededCompletions = 0;
                int dayOfWeek = DateUtil.GetDayOfWeek(DateTime.now());
                for (int day = dayOfWeek; day >= 1; day--) {
                    if (habit.getSchedule().contains(day)) neededCompletions++;
                }

                completionRates[weeksAgo] = (double) completions / (double) neededCompletions * 100;
            }
            else {
                completionRates[weeksAgo] = (double) completions / (double) dueCount * 100;
            }
        }

        completionLatch.countDown();
        if (completionLatch.getCount() <= 0) {
            recreateDataset();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void recreateDataset() {
        completionResultArray.clear();

        for(int i = 6; i >= 0; i--) {
            completionResultArray.add(completionRates[i]);
        }

        datasetChanged();
    }
}
