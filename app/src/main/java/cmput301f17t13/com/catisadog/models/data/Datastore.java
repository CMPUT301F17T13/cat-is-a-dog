package cmput301f17t13.com.catisadog.models.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;

import cmput301f17t13.com.catisadog.models.user.FollowRequest;
import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.models.HabitEvent;
import cmput301f17t13.com.catisadog.models.HabitStatus;
import cmput301f17t13.com.catisadog.models.user.User;

abstract public class Datastore {

    abstract protected void sync(Datastore d);
    abstract protected User fetchUser(String username);
    abstract protected ArrayList<Habit> fetchHabitsForUser();
    abstract protected ArrayList<HabitEvent> fetchHabitEventsForUser();
    abstract protected ArrayList<User> fetchFollowersOfUser();
    abstract protected ArrayList<User> fetchUsersFollowingUser();
    abstract protected ArrayList<User> fetchFollowRequestsMadeByUser();
    abstract protected ArrayList<User> fetchFollowRequestsReceivedByUser();

    abstract protected Habit createHabit(String title, String reason, DateTime startDate, HashSet<Integer> schedule, HabitStatus status);
    abstract protected HabitEvent createHabitEvent(Habit habit, String comment, String photoUrl, DateTime eventDate, User user);
    abstract protected FollowRequest createFollowRequest(User requester, User requestee);
    abstract protected User createUser(String username);

    abstract protected Habit editHabit(Habit habit);
    abstract protected HabitEvent editHabitEvent(HabitEvent habitEvent);

    abstract protected void deleteHabit(Habit habit);
    abstract protected void deleteHabitEvent(HabitEvent habitEvent);
    abstract protected void deleteFollowRequest(FollowRequest followRequest);
}
