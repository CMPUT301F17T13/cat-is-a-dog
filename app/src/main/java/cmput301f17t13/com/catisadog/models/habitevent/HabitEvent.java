/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habitevent;

import android.location.Location;

import org.joda.time.DateTime;

import cmput301f17t13.com.catisadog.models.habit.Habit;

/**
 * Represents a habit event
 */
public class HabitEvent {

    private String key;
    private String userId;
    private Habit habit;
    private String comment;
    private String photoUrl;
    private Location location;
    private DateTime eventDate;

    public HabitEvent() {}

    public HabitEvent(String key, Habit habit, String comment, String photoUrl, Location location, DateTime eventDate, String userId) {
        this.key = key;
        this.userId = userId;
        this.habit = habit;
        this.comment = comment;
        this.photoUrl = photoUrl;
        this.location = location;
        this.eventDate = eventDate;
    }

    public HabitEvent(String key, Habit habit, String userId) {
        this.key = key;
        this.habit = habit;
        this.userId = userId;
        this.eventDate = new DateTime();
    }

    public Habit getHabit() {
        return habit;
    }
    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public DateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(DateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
