package cmput301f17t13.com.catisadog.models;


import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Object representing a habit
 */
public class Habit implements Schedulable, Serializable {

    private String userId;

    /** Brief title */
    private String title;
    /** Brief reason for habit */
    private String reason;

    /** Start date for habit to be due */
    private DateTime startDate;
    /** Days of the week the habit is due */
    private HashSet<Integer> schedule;
    /** Status of the habit */
    private HabitStatus status;

    //TODO(#17): How to handle completion metrics

    public Habit(String userId, String title, String reason, DateTime startDate,
                 HashSet<Integer> schedule, HabitStatus status) {
        this.userId = userId;
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.schedule = schedule;
        this.status = status;
    }

    // Getters and Setters

    public String getUserId() {
        return userId;
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

    public HabitStatus getStatus() {
        return status;
    }

    @Override
    public Set<Integer> getSchedule() {
        return schedule;
    }

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
        return schedule.contains(dayOfWeek);
    }

    /**
     * @return The next due date for the habit
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
