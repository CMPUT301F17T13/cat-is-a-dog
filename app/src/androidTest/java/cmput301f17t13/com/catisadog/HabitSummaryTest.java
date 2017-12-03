/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.matcher.*;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;
import cmput301f17t13.com.catisadog.mocks.MockAuthentication;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;

import static android.support.test.espresso.DataInteraction.*;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class HabitSummaryTest {

    @Rule
    public ActivityTestRule<HabitSummaryActivity> mActivityRule =
            new ActivityTestRule<>(HabitSummaryActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        MockAuthentication.signIn();
        mActivityRule.launchActivity(null);
    }

    @Test
    public void showHabit() throws Exception {
        onData(anything())
                .inAdapterView(withId(R.id.myHabitsListView))
                .atPosition(0)
                .onChildView(withId(R.id.myHabitListItemTitle))
                .check(matches(withText("Test Habit")));
    }
}
