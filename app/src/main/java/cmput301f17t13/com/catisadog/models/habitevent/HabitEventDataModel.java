/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habitevent;

import android.location.Location;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import org.joda.time.DateTime;

import java.util.Locale;

import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.utils.data.FirebaseUtil;

@IgnoreExtraProperties
public class HabitEventDataModel {

    private String key;
    private String userId;
    private String habitKey;
    private String comment;
    private String photoUrl;
    private double latitude;
    private double longitude;
    private long eventDate;

    // Indexes
    public String complete;

    public HabitEventDataModel() {}

    public HabitEventDataModel(HabitEvent event) {
        this.key = event.getKey();
        this.userId = event.getUserId();
        this.habitKey = event.getHabitKey();
        this.comment = event.getComment();
        this.photoUrl = event.getPhotoUrl();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.eventDate = event.getEventDate().getMillis();

        // Create indexes
        this.complete = completeKey(userId, event.getEventDate(), habitKey);
    }

    public static String completeKey(String userId, DateTime date, String key) {
        String strDate = FirebaseUtil.dateToString(date);
        return String.format(Locale.CANADA, "%s_%s_%s", userId, strDate, key);
    }

    /**
     * Convert to a habitKey event object
     * @return the habitKey event object
     */
    @Exclude
    public HabitEvent getHabitEvent() {
        HabitEvent event = new HabitEvent();
        event.setKey(key);
        event.setUserId(userId);
        event.setHabitKey(habitKey);
        event.setComment(comment);
        event.setPhotoUrl(photoUrl);
        event.setLatitude(latitude);
        event.setLongitude(longitude);
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

    public String getHabitKey() {
        return habitKey;
    }

    public void setHabitKey(String habitKey) {
        this.habitKey = habitKey;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getEventDate() {
        return eventDate;
    }

    public void setEventDate(long eventDate) {
        this.eventDate = eventDate;
    }
}
