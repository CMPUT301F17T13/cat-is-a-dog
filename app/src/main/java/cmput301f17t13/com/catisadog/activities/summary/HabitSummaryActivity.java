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

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.BaseDrawerActivity;
import cmput301f17t13.com.catisadog.fragments.summary.FollowingHabitsFragment;
import cmput301f17t13.com.catisadog.fragments.summary.MyHabitsFragment;
import cmput301f17t13.com.catisadog.fragments.summary.TodoHabitsFragment;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.HabitDataSource;
import cmput301f17t13.com.catisadog.models.habit.TodoHabitDataSource;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.utils.data.DataSource;

/**
 * The activity that handles three tabs: my habitEvents, to do habitEvents, and following habitEvents.
 * Stores that habit objects and updates the tab fragments to display the UI.
 */
public class HabitSummaryActivity extends BaseDrawerActivity implements Observer{
    private static final String TAG = "HabitSummaryActivity";

    public ArrayList<Habit> habits;
    public ArrayList<Habit> todoHabits = new ArrayList<>();
    public ViewPagerAdapter adapter;

    private MyHabitsFragment myHabitsFragment;
    private TodoHabitsFragment todoHabitsFragment;

    private String userId;
    private DataSource<Habit> habitDataSource;
    private DataSource<Habit> todoDataSource;

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

        myHabitsFragment = (MyHabitsFragment) adapter.getItem(0);
        todoHabitsFragment = (TodoHabitsFragment) adapter.getItem(1);

        userId = CurrentUser.getInstance().getUserId();

        habitDataSource = new HabitDataSource(userId);
        habitDataSource.addObserver(this);
        habits = habitDataSource.getSource();

        todoDataSource = new TodoHabitDataSource(userId);
        todoDataSource.addObserver(this);
        todoHabits = todoDataSource.getSource();
    }

    /**
     * Navigate to the Add/Edit Habit Activity when the user clicks the floating plus icon
     * @param v the button view
     */
    public void addHabit(View v) {
        Intent intent = new Intent(this, EditHabitActivity.class);
        startActivity(intent);
    }

    /**
     * Notify the fragments that changes have occurred to the habitEvents.
     */
    @Override
    public void update(Observable observable, Object o) {
        String type = (String) o;
        switch (type) {
            case TodoHabitDataSource.SourceType:
                todoHabitsFragment.updateListView();
            default:
                myHabitsFragment.updateListView();
        }
    }

    /**
     * Pager adapter to handle fragment tabs
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
