package cmput301f17t13.com.catisadog;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cmput301f17t13.com.catisadog.activities.history.HabitHistoryActivity;
import cmput301f17t13.com.catisadog.mocks.MockAuthentication;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class HabitHistoryTest {

    @Rule
    public ActivityTestRule<HabitHistoryActivity> mActivityRule =
            new ActivityTestRule<>(HabitHistoryActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        MockAuthentication.signIn();
        mActivityRule.launchActivity(null);
    }

    @Test
    public void showHabitTitle() throws Exception {
        onData(anything())
                .inAdapterView(withId(R.id.list))
                .atPosition(0)
                .onChildView(withId(R.id.myHabitEventListItemTitle))
                .check(matches(withText("Test Habit")));
    }

    @Test
    public void showHabitEventReason() throws Exception {
        onData(anything())
                .inAdapterView(withId(R.id.list))
                .atPosition(0)
                .onChildView(withId(R.id.myHabitEventListItemReason))
                .check(matches(withText("Did it")));
    }

    @Test
    public void showHabitEventDate() throws Exception {

        DateTime testDate = new DateTime(1512199000000l);
        String dateString = testDate.toString("d MMM");

        onData(anything())
                .inAdapterView(withId(R.id.list))
                .atPosition(0)
                .onChildView(withId(R.id.myHabitEventListItemStartDate))
                .check(matches(withText(dateString)));
    }

}