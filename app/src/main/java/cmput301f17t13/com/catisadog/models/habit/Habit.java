/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habit;


import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import cmput301f17t13.com.catisadog.models.HabitStatus;
import cmput301f17t13.com.catisadog.models.Schedulable;

/**
 * Object representing a habit
 */
public class Habit implements Schedulable, Serializable {

    /** Unique identifier */
    private String key;
    /** Owner ID */
    private String userId;

    /** Brief title */
    private String title;
    /** Brief reason for habit */
    private String reason;

    /** Start date for habit to be due */
    private DateTime startDate;
    /** Days of the week the habit is due */
    private Set<Integer> schedule;
    /** Status of the habit */
    private HabitStatus status;
    /** Flag if the habit has been completed today */
    private boolean complete = false;

    //TODO(#17): How to handle completion metrics
    private Double completionRate = null;

    public Habit() {}

    public Habit(String userId, String title, String reason, DateTime startDate,
                 Set<Integer> schedule, HabitStatus status) {
        this.userId = userId;
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.status = status;

        setSchedule(schedule);
    }

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

    public DateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public HabitStatus getStatus() {
        return status;
    }
    public void setStatus(HabitStatus status) {
        this.status = status;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete() {
        this.complete = true;
    }

    public Double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(Double completionRate) {
        this.completionRate = completionRate;
    }

    @Override
    public Set<Integer> getSchedule() {
        return schedule;
    }

    /**
     * Sets the days of the week the habit is due
     * @param days a set of integers from 1 to 7, representing Monday-Sunday as due dates
     */
    @Override
    public void setSchedule(Set<Integer> days) {
        schedule = new HashSet<>(7);
        for (int dayOfWeek = DateTimeConstants.MONDAY; dayOfWeek <= DateTimeConstants.SUNDAY; dayOfWeek++) {
            if (days.contains(dayOfWeek)) {
                schedule.add(dayOfWeek);
            }
        }
    }

    /**
     * Check if habit is due on a particular date
     * @param date the date to test
     * @return the habit is due on date
     */
    @Override
    public boolean isTodo(DateTime date) {
        int dayOfWeek = date.toGregorianCalendar().get(Calendar.DAY_OF_WEEK);
        return schedule.contains(dayOfWeek) && date.getMillis() > startDate.getMillis();
    }

    /**
     * Get the next due date for the habit
     * @return the next due date
     */
    @Override
    public DateTime nextTodo() {
        return null;
    }

    @Override
    public String toString() {
        return "Habit: " + title;
    }
}
