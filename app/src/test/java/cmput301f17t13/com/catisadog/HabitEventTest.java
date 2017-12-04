package cmput301f17t13.com.catisadog;

import android.location.Location;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;

import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;
import cmput301f17t13.com.catisadog.models.HabitStatus;
import cmput301f17t13.com.catisadog.models.user.User;
import cmput301f17t13.com.catisadog.utils.LocationHelper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HabitEventTest {

    private Habit newHabit;
    private User user;

    @Before
    public void setUp() {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, null);
        user = new User("testUser");
    }

    @Test
    public void setComment() throws Exception {

        HabitEvent newHabitEvent = new HabitEvent("1", newHabit.getKey(), user.getUserId());

        //Checks to see if the comment has been updated
        newHabitEvent.setComment("TestComment");
        // Compare string using .equals()
        assertTrue(newHabitEvent.getComment().equals("TestComment"));

        //Checks to see if the invalid comment has been accepted
        //Comments should be no more than 20 chars
        newHabitEvent.setComment("ThisCommentIsWayTooBigToBeAcceptable");
        assertTrue(newHabitEvent.getComment().length() <= 20);
    }

    @Test
    public void setLocation() throws Exception {
        Location location = mock(Location.class);
        when(location.getLatitude()).thenReturn(0.0);
        when(location.getLongitude()).thenReturn(0.0);

        HabitEvent newHabitEvent = new HabitEvent("1", newHabit.getKey(), user.getUserId());
        newHabitEvent.setLatitude(location.getLatitude());
        newHabitEvent.setLongitude(location.getLongitude());

        assertTrue(newHabitEvent.getLatitude() == location.getLatitude());
        assertTrue(newHabitEvent.getLongitude() == location.getLongitude());
    }

}