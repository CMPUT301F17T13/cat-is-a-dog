package cmput301f17t13.com.catisadog.models;


import android.util.SparseBooleanArray;

import com.google.firebase.database.IgnoreExtraProperties;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@IgnoreExtraProperties
public class Habit implements Schedulable, Serializable {

    private String userId;

    private String title;
    private String reason;

    // TODO(#46): Only store timestamp in database
    private DateTime startDate;
    private List<Boolean> schedule;
    private HabitStatus status;

    //TODO(#17): How to handle completion metrics

    public Habit(String userId, String title, String reason, DateTime startDate,
                 HashSet<Integer> schedule, HabitStatus status) {
        this.userId = userId;
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.status = status;

        setSchedule(schedule);
    }

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
    public List<Boolean> getSchedule() {
        return schedule;
    }

    @Override
    public void setSchedule(Set<Integer> days) {
        schedule = new ArrayList<>(7);
        for (int dayOfWeek = DateTimeConstants.MONDAY; dayOfWeek <= DateTimeConstants.SUNDAY; dayOfWeek++) {
            schedule.add(days.contains(dayOfWeek));
        }
    }

    @Override
    public boolean isTodo(DateTime date) {
        int dayOfWeek = date.toGregorianCalendar().get(Calendar.DAY_OF_WEEK);
        return schedule.get(dayOfWeek - 1);
    }

    @Override
    public DateTime nextTodo() {
        return null;
    }

    @Override
    public String toString() {
        return "Habit: " + title;
    }
}
