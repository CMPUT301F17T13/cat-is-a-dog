package cmput301f17t13.com.catisadog;

import android.graphics.drawable.DrawableContainer;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.HashSet;

import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.models.HabitStatus;

import static org.junit.Assert.*;

/**
 * Created by abidrahman on 2017-10-22.
 */
public class HabitTest {
    final static HabitStatus hStatus = HabitStatus.ON_TRACK;

    @Test
    public void setTitle() throws Exception {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        Habit newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, hStatus);

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

        Habit newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, hStatus);

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

        Habit newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, hStatus);

        HashSet<Integer> schedule2 = new HashSet<Integer>();
        schedule2.add(2);
        schedule2.add(3);
        newHabit.setSchedule(schedule2);
        assertTrue(newHabit.getSchedule() == schedule2);
    }

}