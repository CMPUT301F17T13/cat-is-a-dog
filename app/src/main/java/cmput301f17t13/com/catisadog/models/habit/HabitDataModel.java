/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habit;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cmput301f17t13.com.catisadog.models.HabitStatus;
import cmput301f17t13.com.catisadog.utils.data.FirebaseUtil;

/**
 * Represents the Firebase data model for the {@link Habit} class
 * @see Habit
 */
@IgnoreExtraProperties
public class HabitDataModel {

    private String key;
    private String userId;

    private String title;
    private String reason;

    // Start date store as milliseconds since epoch
    private long startDate;
    // List of booleans matching index (day) to if it repeats
    private List<Boolean> schedule;

    private Double completionRate;

    /** Empty constructor for firebase */
    public HabitDataModel() {}

    /**
     * Convert a habit to its data model
     * @param habit the habit to convert
     */
    public HabitDataModel(Habit habit) {
        key = habit.getKey();
        userId = habit.getUserId();
        title = habit.getTitle();
        reason = habit.getReason();

        startDate = habit.getStartDate().getMillis();

        schedule = new ArrayList<>(DateTimeConstants.DAYS_PER_WEEK);
        for (int i = 0; i < DateTimeConstants.DAYS_PER_WEEK; i++) {
            schedule.add(habit.getSchedule().contains(i + 1));
        }

        completionRate = habit.getCompletionRate();
    }

    /**
     * Convert to a habit
     * @return the corresponding Habit object
     */
    @Exclude
    public Habit getHabit() {
        HashSet<Integer> newSchedule = new HashSet<>(DateTimeConstants.DAYS_PER_WEEK);
        for(int i = 0; i < schedule.size(); i++) {
            if(schedule.get(i)) {
                newSchedule.add(i + 1);
            }
        }

        Habit habit = new Habit();
        habit.setKey(key);
        habit.setUserId(userId);
        habit.setTitle(title);
        habit.setReason(reason);
        habit.setStartDate(new DateTime(startDate));
        habit.setSchedule(newSchedule);
        habit.setCompletionRate(completionRate);

        if (completionRate != null) {
            habit.setStatus(HabitStatus.fromCompletionRate(completionRate));
        }

        return habit;
    }

    /* Getters and Setters */

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }
}
