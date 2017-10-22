package cmput301f17t13.com.catisadog.models.data;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;

import cmput301f17t13.com.catisadog.models.user.FollowRequest;
import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.models.HabitEvent;
import cmput301f17t13.com.catisadog.models.HabitStatus;
import cmput301f17t13.com.catisadog.models.user.User;

public class FirebaseDatastore extends Datastore {
    @Override
    protected void sync(Datastore d) {

    }

    @Override
    protected User fetchUser(String username) {
        return null;
    }

    @Override
    protected ArrayList<Habit> fetchHabitsForUser() {
        return null;
    }

    @Override
    protected ArrayList<HabitEvent> fetchHabitEventsForUser() {
        return null;
    }

    @Override
    protected ArrayList<User> fetchFollowersOfUser() {
        return null;
    }

    @Override
    protected ArrayList<User> fetchUsersFollowingUser() {
        return null;
    }

    @Override
    protected ArrayList<User> fetchFollowRequestsMadeByUser() {
        return null;
    }

    @Override
    protected ArrayList<User> fetchFollowRequestsReceivedByUser() {
        return null;
    }

    @Override
    protected Habit createHabit(String title, String reason, DateTime startDate, HashSet<Integer> schedule, HabitStatus status) {
        return null;
    }

    @Override
    protected HabitEvent createHabitEvent(Habit habit, String comment, String photoUrl, DateTime eventDate, User user) {
        return null;
    }

    @Override
    protected FollowRequest createFollowRequest(User requester, User requestee) {
        return null;
    }

    @Override
    protected User createUser(String username) {
        return null;
    }

    @Override
    protected Habit editHabit(Habit habit) {
        return null;
    }

    @Override
    protected HabitEvent editHabitEvent(HabitEvent habitEvent) {
        return null;
    }

    @Override
    protected void deleteHabit(Habit habit) {

    }

    @Override
    protected void deleteHabitEvent(HabitEvent habitEvent) {

    }

    @Override
    protected void deleteFollowRequest(FollowRequest followRequest) {

    }
}
