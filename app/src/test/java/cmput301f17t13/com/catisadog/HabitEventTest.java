package cmput301f17t13.com.catisadog;

import android.location.Location;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;
import cmput301f17t13.com.catisadog.models.HabitStatus;
import cmput301f17t13.com.catisadog.models.user.User;
import cmput301f17t13.com.catisadog.utils.LocationHelper;

import static org.junit.Assert.*;

public class HabitEventTest {

    private Habit newHabit;
    private User user;

    @Before
    public void setUP() {
        DateTime now = new DateTime();
        HashSet<Integer> schedule = new HashSet<>(1,2);

        newHabit = new Habit("1", "Test Habit", "Test Reason", now, schedule, HabitStatus.ON_TRACK);
        user = new User("testUser");
    }

    @Test
    public void getHabit() throws Exception {

        //Check to see if new HabitEvent is linked to the correct habit.
        HabitEvent newHabitEvent = new HabitEvent("1", newHabit.getKey(), user.getUserId());
        assertTrue(newHabitEvent.getHabitKey().equals(newHabit.getKey()));

        //Adding a habitEvent with a null habit, should not be allowed
        HabitEvent invalidEvent = new HabitEvent("1", null, user.getUserId());
        assertNull(invalidEvent);

        //Adding a habitEvent with a null user, should not be allowed
        HabitEvent invalidEventUser = new HabitEvent("1", newHabit.getKey(), null);
        assertNull(invalidEventUser);
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
        Location location = LocationHelper.getLocation();
        HabitEvent newHabitEvent = new HabitEvent("1", newHabit.getKey(), user.getUserId());
        newHabitEvent.setLatitude(location.getLatitude());
        newHabitEvent.setLongitude(location.getLongitude());

        assertTrue(newHabitEvent.getLatitude() == location.getLatitude());
        assertTrue(newHabitEvent.getLongitude() == location.getLongitude());
    }

    @Test
    public void setPhotoUrl() throws Exception {

        HabitEvent newHabitEvent = new HabitEvent("1", newHabit.getKey(), user.getUserId());

        newHabitEvent.setPhotoUrl("TestPhotoUrl.com");

        // Compare string using .equals()
        assertTrue(newHabitEvent.getPhotoUrl().equals("TestPhotoUrl.com"));

        //Check adding a non url link to PhotoUrl
        newHabitEvent.setPhotoUrl("notaURL");
        // Compare string using .equals()
        assertFalse(newHabitEvent.getPhotoUrl().equals("notaURL"));
    }

}