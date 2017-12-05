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
import cmput301f17t13.com.catisadog.utils.data.Repository;
import cmput301f17t13.com.catisadog.utils.date.DateUtil;
import cmput301f17t13.com.catisadog.utils.date.Week;

public class CompletionMetricDataSource extends DataSource<Double>
        implements ValueEventListener {

    private Habit habit;
    private double[] completionRates;
    private ArrayList<Double> completionResultArray;
    private CountDownLatch completionLatch;
    private ArrayList<Query> queryRegister;
    private Repository<Habit> habitRepository;

    public CompletionMetricDataSource(Habit habit) {
        this.habit = habit;
        completionResultArray = new ArrayList<>();
        queryRegister = new ArrayList<>();
        habitRepository = new HabitRepository(habit.getUserId());
    }

    @Override
    public void open() {
        completionResultArray.clear();
        completionRates = new double[4];
        completionLatch = new CountDownLatch(4);

        if(habit.getSchedule().size() > 0) {
            for(Week week : DateUtil.GetNPastWeeks(DateTime.now(), 4)) {
                String startKey = HabitEventDataModel.habitStampKey(habit.getKey(), week.getStartOfWeek());
                String endKey = HabitEventDataModel.habitStampKey(habit.getKey(), week.getEndOfWeek());

                Query eventCountQuery = FirebaseDatabase.getInstance().getReference("events/" + habit.getUserId())
                        .orderByChild("habitStamp").startAt(startKey).endAt(endKey);

                eventCountQuery.addValueEventListener(this);
                queryRegister.add(eventCountQuery);
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
        close();
    }

    private void recreateDataset() {
        completionResultArray.clear();

        for(int i = 3; i >= 0; i--) {
            completionResultArray.add(completionRates[i]);
        }

        Double cumulativeCompletionRate = 0.0;
        int weeksOld = DateUtil.WeekDifference(habit.getStartDate(), DateTime.now());
        int weeksCounted = Math.min(3, weeksOld + 1);

        for (int i = 0; i < weeksCounted; i++) {
            cumulativeCompletionRate += completionRates[i];
        }

        cumulativeCompletionRate = cumulativeCompletionRate / weeksCounted;
        publishCompletionRate(cumulativeCompletionRate);

        datasetChanged();
    }

    private void publishCompletionRate(Double completionRate) {
        habit.setCompletionRate(completionRate);
        habitRepository.update(habit.getKey(), habit);
    }

    @Override
    public void close() {
        for (Query q : queryRegister) {
            q.removeEventListener(this);
        }
        queryRegister.clear();
    }
}