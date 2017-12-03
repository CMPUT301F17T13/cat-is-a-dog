package cmput301f17t13.com.catisadog;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;

import cmput301f17t13.com.catisadog.activities.summary.ViewHabitActivity;
import cmput301f17t13.com.catisadog.mocks.MockAuthentication;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ViewHabitActivityTest {

    @Rule
    public ActivityTestRule<ViewHabitActivity> mActivityRule =
            new ActivityTestRule<>(ViewHabitActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        MockAuthentication.signIn();

        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();

        Intent launch = new Intent();
        launch.putExtra(targetContext.getString(R.string.VIEW_HABIT_HABIT_KEY), "1");
        launch.putExtra(targetContext.getString(R.string.VIEW_HABIT_USER_ID), "mockUserId");

        mActivityRule.launchActivity(launch);
    }

    @Test
    public void testTitle() {
        String title = "Test Habit";
        onView(withId(R.id.habitTitle)).check(matches(withText(title)));
    }

    @Test
    public void testReason() {
        String reason = "Test Reason";
        onView(withId(R.id.habitReason)).check(matches(withText(reason)));
    }

    @Test
    public void testStartDate() {
        DateTime expectedDate = new DateTime(1512198000000L);
        onView(withId(R.id.habitStartValue)).check(matches(withText(expectedDate.toString("EEEE MMMM dd, yyyy"))));
    }

    @Test
    public void testSchedule() {
        HashSet<Integer> schedule = new HashSet<>();
        schedule.add(2);
        schedule.add(3);

        String repeatedDays;

        if (schedule.isEmpty()) {
            repeatedDays = "Never";
        } else if (schedule.size() == 7) {
            repeatedDays = "Every day";
        } else if (schedule.size() == 5 &&
                schedule.contains(DateTimeConstants.MONDAY) &&
                schedule.contains(DateTimeConstants.TUESDAY) &&
                schedule.contains(DateTimeConstants.WEDNESDAY) &&
                schedule.contains(DateTimeConstants.THURSDAY) &&
                schedule.contains(DateTimeConstants.FRIDAY)) {
            repeatedDays = "Weekdays";
        } else if (schedule.size() == 2 &&
                schedule.contains(DateTimeConstants.SATURDAY) &&
                schedule.contains(DateTimeConstants.SUNDAY)) {
            repeatedDays = "Weekends";
        } else {
            ArrayList<String> dates = new ArrayList<>();
            if (schedule.contains(DateTimeConstants.SUNDAY)) {
                dates.add("Sun");
            }
            if (schedule.contains(DateTimeConstants.MONDAY)) {
                dates.add("Mon");
            }
            if (schedule.contains(DateTimeConstants.TUESDAY)) {
                dates.add("Tue");
            }
            if (schedule.contains(DateTimeConstants.WEDNESDAY)) {
                dates.add("Wed");
            }
            if (schedule.contains(DateTimeConstants.THURSDAY)) {
                dates.add("Thu");
            }
            if (schedule.contains(DateTimeConstants.FRIDAY)) {
                dates.add("Fri");
            }
            if (schedule.contains(DateTimeConstants.SATURDAY)) {
                dates.add("Sat");
            }
            repeatedDays = TextUtils.join(", ", dates);
        }

        onView(withId(R.id.habitRepeatsValue)).check(matches(withText(repeatedDays)));
    }

}
