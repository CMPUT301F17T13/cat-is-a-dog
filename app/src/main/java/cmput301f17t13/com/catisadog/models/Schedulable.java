package cmput301f17t13.com.catisadog.models;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Set;

public interface Schedulable {

    /** Gets days of week to repeat on */
    List<Boolean> getSchedule();

    /** Sets days of week to repeat on */
    void setSchedule(Set<Integer> days);

    boolean isTodo(DateTime date);

    DateTime nextTodo();
}
