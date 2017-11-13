package cmput301f17t13.com.catisadog;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.models.HabitStatus;

import static org.junit.Assert.*;

public class HabitTest {

    Habit newHabit;

    @Before
    public void setUp() {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, null);

    }

    @Test
    public void setTitle() throws Exception {

        //Checks to see if the new title has been updated
        newHabit.setTitle("SampleTitle");
        // Compare string using .equals()
        assertTrue(newHabit.getTitle().equals("SampleTitle"));

        //Checks to see if the invalid title has been accepted
        //Titles should be no more than 20 chars
        newHabit.setTitle("ThisTitleIsWayTooBigTooBeAcceptable");
        assertTrue(newHabit.getTitle().length() <= 20);
    }

    @Test
    public void setReason() throws Exception {

        //Checks to see if the reason has been updated
        newHabit.setReason("SampleReason");
        // Compare string using .equals()
        assertTrue(newHabit.getReason().equals("SampleReason"));

        //Checks to see if the invalid reason has been accepted
        //Reasons should be no more than 30 chars
        newHabit.setReason("ThisReasonIsWayTooBigToBeAcceptable");
        assertTrue(newHabit.getReason().length() <= 30);
    }

    @Test
    public void setSchedule() throws Exception {

        //Check for updated schedule.
        // Explicit type argument replaced with <>
        HashSet<Integer> schedule2 = new HashSet<>();
        schedule2.add(2);
        schedule2.add(3);
        newHabit.setSchedule(schedule2);
        assertTrue(newHabit.getSchedule().equals(schedule2));

        //Check for updating schedule with invalid dates (update with error handling)
        HashSet<Integer> scheduleInvalid = new HashSet<>();
        scheduleInvalid.add(9);
        scheduleInvalid.add(21);
        newHabit.setSchedule(scheduleInvalid);
        assertFalse(newHabit.getSchedule() == scheduleInvalid);

        //Check for updating schedule with empty schedule
        HashSet<Integer> scheduleEmpty = new HashSet<>();
        assertFalse(newHabit.getSchedule() == scheduleEmpty);

    }

}