package cmput301f17t13.com.catisadog;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.location.Location;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.HashSet;

import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.models.HabitEvent;
import cmput301f17t13.com.catisadog.models.HabitStatus;
import cmput301f17t13.com.catisadog.models.user.User;

import static org.junit.Assert.*;

/**
 * Created by abidrahman on 2017-10-22.
 */
public class HabitEventTest {
    final static HabitStatus hStatus = HabitStatus.ON_TRACK;

    @Test
    public void getHabit() throws Exception {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        Habit newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, hStatus);
        User user = new User("testUser");

        HabitEvent newHabitEvent = new HabitEvent("1", newHabit, user);

        assertTrue(newHabitEvent.getHabit() == newHabit);
    }

    @Test
    public void setComment() throws Exception {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        Habit newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, hStatus);
        User user = new User("testUser");

        HabitEvent newHabitEvent = new HabitEvent("1", newHabit, user);

        //Checks to see if the comment has been updated
        newHabitEvent.setComment("TestComment");
        assertTrue(newHabitEvent.getComment() == "TestComment");

        //Checks to see if the invalid comment has been accepted
        //Comments should be no more than 20 chars
        newHabitEvent.setComment("ThisCommentIsWayTooBigToBeAcceptable");
        assertTrue(newHabitEvent.getComment().length() <= 20);

    }

    @Test
    public void setLocation() throws Exception {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        Habit newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, hStatus);
        User user = new User("testUser");

        Location location = new Location("TestLocation");
        HabitEvent newHabitEvent = new HabitEvent("1", newHabit, user);
        newHabitEvent.setLocation(location);

        assertTrue(newHabitEvent.getLocation() == location);
    }

    @Test
    public void setPhotoUrl() throws Exception {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        Habit newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, hStatus);
        User user = new User("testUser");

        HabitEvent newHabitEvent = new HabitEvent("1", newHabit, user);
        newHabitEvent.setPhotoUrl("TestPhotoUrl.com");

        assertTrue(newHabitEvent.getPhotoUrl() == "TestPhotoUrl.com");
    }

}