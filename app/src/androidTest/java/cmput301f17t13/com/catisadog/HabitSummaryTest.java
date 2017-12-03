/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog;

import android.os.SystemClock;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.*;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;
import cmput301f17t13.com.catisadog.activities.summary.ViewHabitActivity;
import cmput301f17t13.com.catisadog.mocks.MockAuthentication;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;

import static android.support.test.espresso.DataInteraction.*;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
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

    @Test
    public void viewHabit() throws Exception {
        Intents.init();
        onData(anything())
                .inAdapterView(withId(R.id.myHabitsListView))
                .atPosition(0)
                .perform(click());

        intended(hasComponent(ViewHabitActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void todoHabit() throws Exception {
        changeTab("Todo");

        onData(anything())
                .inAdapterView(withId(R.id.todoHabitsListView))
                .atPosition(0)
                .onChildView(withId(R.id.todoHabitListItemTitle))
                .check(matches(withText("Test Habit")));
    }

    private void changeTab(String tabTitle) {
        Matcher<View> tabMatcher = allOf(withText(tabTitle),
                isDescendantOfA(withId(R.id.habitSummaryTabs)));

        onView(tabMatcher)
                .perform(click());

        SystemClock.sleep(800);
    }
}
