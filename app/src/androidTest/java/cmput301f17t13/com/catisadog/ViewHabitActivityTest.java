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
import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.models.HabitStatus;
import cmput301f17t13.com.catisadog.utils.IntentConstants;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ViewHabitActivityTest {

    private String title;
    private String reason;
    private DateTime now;
    private HashSet<Integer> schedule;

    @Rule
    public ActivityTestRule<ViewHabitActivity> mActivityRule =
            new ActivityTestRule<ViewHabitActivity>(ViewHabitActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    now = new DateTime();
                    schedule = new HashSet<>();
                    schedule.add(2);
                    schedule.add(3);
                    title = "Test Habit";
                    reason = "Test Reason";

                    Habit newHabit = new Habit("1", title, reason, now, schedule, null);

                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, ViewHabitActivity.class);
                    result.putExtra(IntentConstants.VIEW_HABIT_INTENT_DATA, newHabit);
                    return result;
                }
            };


    @Test
    public void testTitle() {
        onView(withId(R.id.habitTitle)).check(matches(withText(title)));
    }

    @Test
    public void testReason() {
        onView(withId(R.id.habitReason)).check(matches(withText(reason)));
    }

    @Test
    public void testStartDate() {
        onView(withId(R.id.habitStartValue)).check(matches(withText(now.toString("EEEE MMMM dd, yyyy"))));
    }

    @Test
    public void testSchedule() {
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
