package cmput301f17t13.com.catisadog;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cmput301f17t13.com.catisadog.activities.summary.AddHabitEventActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddHabitEventActivityTest {

    @Rule
    public IntentsTestRule<AddHabitEventActivity> mActivityRule =
            new IntentsTestRule<>(AddHabitEventActivity.class);

    @Test
    public void addImage() {
        Intent resultData = new Intent();
        Bitmap icon = BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(),
                R.drawable.mock_camera_image);
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(not(isInternal())).respondWith(result);

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.imageOpacityOverlay)).perform(click());
        onView(withId(R.id.imageOpacityOverlay)).check(matches(not(isDisplayed())));

        onView(withId(R.id.imageDelete)).perform(click());
        onView(withId(R.id.imageOpacityOverlay)).check(matches(isDisplayed()));
    }
}
