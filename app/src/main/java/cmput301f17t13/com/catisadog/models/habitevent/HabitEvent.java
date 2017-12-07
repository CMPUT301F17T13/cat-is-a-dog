/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.habitevent;

import android.location.Location;

import org.joda.time.DateTime;

import java.io.Serializable;

import cmput301f17t13.com.catisadog.models.habit.Habit;

/**
 * Represents a habit event
 */
public class HabitEvent implements Serializable {

    private String key;         // The database key
    private String userId;      // The owner
    private String habitKey;    // The parent habit key
    private String comment;     // The event comment
    private String photoUrl;    // Url for the photo (currently base64 store of the image)
    private double latitude;    // The location latitude (default 0)
    private double longitude;   // The location longitude (default 0)
    private DateTime eventDate; // The event timestamp

    public HabitEvent() {}

    public HabitEvent(String key, String habitKey, String comment, String photoUrl, double lat, double lon, DateTime eventDate, String userId) {
        this.key = key;
        this.userId = userId;
        this.habitKey = habitKey;
        this.comment = comment;
        this.photoUrl = photoUrl;
        this.latitude = lat;
        this.longitude = lon;
        this.eventDate = eventDate;
    }

    public HabitEvent(String key, String habitKey, String userId) {
        this.key = key;
        this.habitKey = habitKey;
        this.userId = userId;
        this.eventDate = new DateTime();
    }

    /** Getters and Setters */

    public String getHabitKey() {
        return habitKey;
    }
    public void setHabitKey(String habitKey) {
        this.habitKey = habitKey;
    }

    public String getComment() {
        return comment;
    }

    // Comment cannot be longer than 20 characters in length
    public void setComment(String comment) {
        if(comment.length() > 20) {
            comment = comment.substring(0, 19);
        }

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
