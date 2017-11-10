package cmput301f17t13.com.catisadog.models;


import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Habit implements Schedulable, Serializable {

    private String title;
    private String reason;

    private DateTime startDate;
    private HashSet<Integer> schedule;
    private HabitStatus status;

    String id;

    //TODO(#17): How to handle completion metrics

    public Habit(String id, String title, String reason, DateTime startDate,
                 HashSet<Integer> schedule, HabitStatus status) {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.schedule = schedule;
        this.status = status;
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
    public HashSet<Integer> getSchedule() {
        return schedule;
    }

    @Override
    public void setSchedule(Collection<Integer> days) {
        // Fix unchecked cast
        this.schedule = (HashSet<Integer>)days;
    }

    @Override
    public boolean isTodo(DateTime date) {
        for (Integer day : schedule) {
            if (day == date.toGregorianCalendar().get(Calendar.DAY_OF_WEEK)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DateTime nextTodo() {
        return null;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Habit: " + title;
    }
}
