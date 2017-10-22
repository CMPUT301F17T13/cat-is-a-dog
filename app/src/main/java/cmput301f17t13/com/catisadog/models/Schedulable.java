package cmput301f17t13.com.catisadog.models;

import org.joda.time.DateTime;

import java.util.Collection;

public interface Schedulable {

    /** Gets days of week to repeat on */
    Collection<Integer> getSchedule();

    /** Sets days of week to repeat on */
    void setSchedule(Collection<Integer> days);

    boolean isTodo(DateTime date);

    DateTime nextTodo();
}
