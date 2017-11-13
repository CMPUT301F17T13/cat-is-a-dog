/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@IgnoreExtraProperties
public class HabitDataModel {

    private String userId;

    private String title;
    private String reason;

    private long startDate;
    private List<Boolean> schedule;
    private HabitStatus status;

    /** Empty constructor for firebase */
    public HabitDataModel() {}

    public HabitDataModel(Habit habit) {
        userId = habit.getUserId();
        title = habit.getTitle();
        reason = habit.getReason();

        startDate = habit.getStartDate().getMillis();

        schedule = new ArrayList<>(7);
        for (int i = 0; i < DateTimeConstants.DAYS_PER_WEEK; i++) {
            schedule.add(habit.getSchedule().contains(i + 1));
        }
    }

    @Exclude
    public Habit getHabit() {
        HashSet<Integer> newSchedule = new HashSet<>(7);
        for(int i = 0; i < schedule.size(); i++) {
            if(schedule.get(i)) {
                newSchedule.add(i + 1);
            }
        }

        return new Habit(
                userId,
                title,
                reason,
                new DateTime(startDate),
                newSchedule,
                status
        );
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public List<Boolean> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Boolean> schedule) {
        this.schedule = schedule;
    }

    public HabitStatus getStatus() {
        return status;
    }

    public void setStatus(HabitStatus status) {
        this.status = status;
    }
}
