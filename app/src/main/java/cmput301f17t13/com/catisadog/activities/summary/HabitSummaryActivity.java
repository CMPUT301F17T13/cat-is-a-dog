/*
 * Habit Summary Activity
 *
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.activities.summary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.BaseDrawerActivity;
import cmput301f17t13.com.catisadog.fragments.summary.FollowingHabitsFragment;
import cmput301f17t13.com.catisadog.fragments.summary.MyHabitsFragment;
import cmput301f17t13.com.catisadog.fragments.summary.TodoHabitsFragment;
import cmput301f17t13.com.catisadog.utils.ViewPagerAdapter;

/**
 * The activity that handles three tabs: my habitEvents, to do habitEvents, and following habitEvents.
 * Stores that habit objects and updates the tab fragments to display the UI.
 */
public class HabitSummaryActivity extends BaseDrawerActivity {

    public ViewPagerAdapter adapter;

    //private MyHabitsFragment myHabitsFragment;
    //private TodoHabitsFragment todoHabitsFragment;

    /**
     * Set up tab layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_summary);
        drawToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.habitSummaryPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MyHabitsFragment(), "My Habits");
        adapter.addFragment(new TodoHabitsFragment(), "Todo");
        adapter.addFragment(new FollowingHabitsFragment(), "Following");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.habitSummaryTabs);
        tabLayout.setupWithViewPager(viewPager);

        //myHabitsFragment = (MyHabitsFragment) adapter.getItem(0);
        //todoHabitsFragment = (TodoHabitsFragment) adapter.getItem(1);
    }

    /**
     * Navigate to the Add/Edit Habit Activity when the user clicks the floating plus icon
     * @param v the button view
     */
    public void addHabit(View v) {
        Intent intent = new Intent(this, EditHabitActivity.class);
        startActivity(intent);
    }

}
