package cmput301f17t13.com.catisadog.models;

import android.location.Location;

import org.joda.time.DateTime;

import cmput301f17t13.com.catisadog.models.user.User;

public class HabitEvent {

    private Habit habit;
    private String comment;
    private String photoUrl;
    private Location location;
    private DateTime eventDate;
    private User user;
    private String id;

    public HabitEvent(String id, Habit habit, String comment, String photoUrl, DateTime eventDate, User user) {
        this.habit = habit;
        this.comment = comment;
        this.photoUrl = photoUrl;
        this.eventDate = eventDate;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public String getId() {
        return id;
    }
}
