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
import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.models.HabitDataSource;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.utils.IntentConstants;
import cmput301f17t13.com.catisadog.utils.data.DataSource;

public class HabitSummaryActivity extends BaseDrawerActivity implements Observer{
    private static final String TAG = "HabitSummaryActivity";

    public ArrayList<Habit> habits;
    public ArrayList<Habit> todoHabits = new ArrayList<>();
    public ViewPagerAdapter adapter;

    private MyHabitsFragment myHabitsFragment;
    private TodoHabitsFragment todoHabitsFragment;

    private DataSource<Habit> habitDataSource;

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

        habitDataSource = new HabitDataSource(CurrentUser.getInstance().getUserId());
        habitDataSource.addObserver(this);
        habits = habitDataSource.getSource();
    }

    /**
     * Navigate to the Add/Edit Habit Activity when the user clicks the floating plus icon
     * @param v the button view
     */
    public void addHabit(View v) {
        Intent intent = new Intent(this, EditHabitActivity.class);
        startActivity(intent);
    }

    @Override
    public void update(Observable observable, Object o) {
        calculateTodoHabits();
        myHabitsFragment.updateListView();
        todoHabitsFragment.updateListView();
    }

    /**
     * Calculate the habits that are to-do today and add them to a new array
     */
    private void calculateTodoHabits() {
        todoHabits.clear();
        for (int i = 0; i < habits.size(); i++) {
            if (habits.get(i).isTodo(new DateTime())) {
                todoHabits.add(habits.get(i));
                Log.d("todo", Integer.valueOf(i).toString());
            }
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
