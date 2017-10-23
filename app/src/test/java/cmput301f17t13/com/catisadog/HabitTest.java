package cmput301f17t13.com.catisadog;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.HashSet;

import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.models.HabitStatus;

import static org.junit.Assert.*;

public class HabitTest {

    @Test
    public void setTitle() throws Exception {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        Habit newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, HabitStatus.ON_TRACK);

        //Checks to see if the new title has been updated
        newHabit.setTitle("SampleTitle");
        assertTrue(newHabit.getTitle() == "SampleTitle");

        //Checks to see if the invalid title has been accepted
        //Titles should be no more than 20 chars
        newHabit.setTitle("ThisTitleIsWayTooBigTooBeAcceptable");
        assertTrue(newHabit.getTitle().length() <= 20);
    }

    @Test
    public void setReason() throws Exception {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        Habit newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, HabitStatus.ON_TRACK);

        //Checks to see if the reason has been updated
        newHabit.setReason("SampleReason");
        assertTrue(newHabit.getReason() == "SampleReason");

        //Checks to see if the invalid reason has been accepted
        //Reasons should be no more than 30 chars
        newHabit.setReason("ThisReasonIsWayTooBigToBeAcceptable");
        assertTrue(newHabit.getReason().length() <= 30);
    }

    @Test
    public void setSchedule() throws Exception {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        Habit newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, HabitStatus.ON_TRACK);

        //Check for updated schedule.
        HashSet<Integer> schedule2 = new HashSet<Integer>();
        schedule2.add(2);
        schedule2.add(3);
        newHabit.setSchedule(schedule2);
        assertTrue(newHabit.getSchedule() == schedule2);

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