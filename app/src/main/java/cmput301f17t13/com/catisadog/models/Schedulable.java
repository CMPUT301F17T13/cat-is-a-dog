package cmput301f17t13.com.catisadog.models;

import org.joda.time.DateTime;

import java.util.Set;

/**
 * Indicates an object has a set weekly schedule, and is due on certain days
 */
public interface Schedulable {

    /** Gets days of week to repeat on */
    Set<Integer> getSchedule();

    /** Sets days of week to repeat on */
    void setSchedule(Set<Integer> days);

    boolean isTodo(DateTime date);

    DateTime nextTodo();
}
