/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cmput301f17t13.com.catisadog.activities.social.SocialActivity;
import cmput301f17t13.com.catisadog.mocks.MockAuthentication;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class SocialTest {

    @Rule
    public ActivityTestRule<SocialActivity> mActivityRule =
            new ActivityTestRule<>(SocialActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        MockAuthentication.signIn();
        mActivityRule.launchActivity(null);
    }

    @Test
    public void showFollowing() throws Exception {
        onData(anything())
                .inAdapterView(withId(R.id.followingListView))
                .atPosition(0)
                .onChildView(withId(R.id.userDisplayName))
                .check(matches(withText("Kevin")));

        onData(anything())
                .inAdapterView(withId(R.id.followingListView))
                .atPosition(0)
                .onChildView(withId(R.id.socialActionButton))
                .check(matches(withText("Following")));
    }

    @Test
    public void showFollowers() throws Exception {
        changeTab("Followers");

        onData(anything())
                .inAdapterView(withId(R.id.followersListView))
                .atPosition(0)
                .onChildView(withId(R.id.userDisplayName))
                .check(matches(withText("James")));

        onData(anything())
                .inAdapterView(withId(R.id.followersListView))
                .atPosition(0)
                .onChildView(withId(R.id.socialActionButton))
                .check(matches(withText("Follow")));
    }

    @Test
    public void showFollowRequests() throws Exception {
        changeTab("Requests");

        onData(anything())
                .inAdapterView(withId(R.id.requestsListView))
                .atPosition(0)
                .onChildView(withId(R.id.userDisplayName))
                .check(matches(withText("Nathan")));
    }

    @Test
    public void showUsers() throws Exception {
        changeTab("Search");

        onData(anything())
                .inAdapterView(withId(R.id.usersListView))
                .atPosition(0)
                .onChildView(withId(R.id.userDisplayName))
                .check(matches(anyOf(withText("Kevin"), withText("Nathan"), withText("James"))));
    }


    private void changeTab(String tabTitle) {
        Matcher<View> tabMatcher = allOf(withText(tabTitle),
                isDescendantOfA(withId(R.id.socialTabs)));

        onView(tabMatcher)
                .perform(click());

        SystemClock.sleep(2000);
    }
}
