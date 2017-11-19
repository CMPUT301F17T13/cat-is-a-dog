/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habitevent;

import android.location.Location;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.joda.time.DateTime;

import cmput301f17t13.com.catisadog.models.habit.Habit;

@IgnoreExtraProperties
public class HabitEventDataModel {

    private String key;
    private String userId;
    private String habit;
    private String comment;
    private String photoUrl;
    private Location location;
    private long eventDate;

    public HabitEventDataModel() {}

    public HabitEventDataModel(HabitEvent event) {
        this.key = event.getKey();
        this.userId = event.getUserId();
        this.habit = event.getHabit().getKey();
        this.comment = event.getComment();
        this.photoUrl = event.getPhotoUrl();
        this.location = event.getLocation();
        this.eventDate = event.getEventDate().getMillis();
    }

    @Exclude
    public HabitEvent getHabitEvent() {
        HabitEvent event = new HabitEvent();
        event.setKey(key);
        event.setUserId(userId);
        event.setHabit(new Habit());
        event.setComment(comment);
        event.setPhotoUrl(photoUrl);
        event.setLocation(location);
        event.setEventDate(new DateTime(eventDate));

        return event;
    }

    /* Getters and Setters */

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser() {
        return userId;
    }

    public void setUser(String user) {
        this.userId = user;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
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

    public long getEventDate() {
        return eventDate;
    }

    public void setEventDate(long eventDate) {
        this.eventDate = eventDate;
    }
}
