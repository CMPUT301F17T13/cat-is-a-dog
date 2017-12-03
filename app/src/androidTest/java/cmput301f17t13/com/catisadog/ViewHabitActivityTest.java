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
        onView(withId(R.id.habitRepeatsValue)).check(matches(withText("Every day")));
    }

}
