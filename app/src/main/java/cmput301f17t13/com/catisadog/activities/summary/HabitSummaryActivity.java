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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.fragments.summary.FollowingHabitsFragment;
import cmput301f17t13.com.catisadog.fragments.summary.MyHabitsFragment;
import cmput301f17t13.com.catisadog.fragments.summary.TodoHabitsFragment;
import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.utils.IntentConstants;

public class HabitSummaryActivity extends AppCompatActivity {

    public ArrayList<Habit> habits = new ArrayList<>();
    public ArrayList<Habit> todoHabits = new ArrayList<>();
    public ViewPagerAdapter adapter;

    /**
     * Set up tab layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_summary);

        ViewPager viewPager = (ViewPager) findViewById(R.id.habitSummaryPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MyHabitsFragment(), "My Habits");
        adapter.addFragment(new TodoHabitsFragment(), "Todo");
        adapter.addFragment(new FollowingHabitsFragment(), "Following");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.habitSummaryTabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Navigate to the Add Habit Activity when the user clicks the floating plus icon
     * @param v the button view
     */
    public void addHabit(View v) {
        Intent intent = new Intent(this, AddHabitActivity.class);
        startActivityForResult(intent, IntentConstants.ADD_HABIT_INTENT_REQUEST);
    }

    /**
     * Handle the data returned from worker activities created by this activity
     * (e.g. AddHabitActivity)
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == IntentConstants.ADD_HABIT_INTENT_RESULT) {
            Habit habit = (Habit) data.getSerializableExtra(IntentConstants.ADD_HABIT_INTENT_DATA);
            habits.add(habit);
        }

        calculateTodoHabits();

        MyHabitsFragment myHabitsFragment = (MyHabitsFragment) adapter.getItem(0);
        myHabitsFragment.updateListView();

        TodoHabitsFragment todoHabitsFragment = (TodoHabitsFragment) adapter.getItem(1);
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
