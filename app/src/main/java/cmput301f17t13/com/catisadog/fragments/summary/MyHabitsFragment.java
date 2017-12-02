/*
 * My Habits Fragment
 *
 * Copyright (c) 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.fragments.summary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;
import cmput301f17t13.com.catisadog.activities.summary.ViewHabitActivity;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.HabitDataSource;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.utils.data.DataSource;

/**
 * A screen to view all the current user's habits
 */

public class MyHabitsFragment extends Fragment
        implements Observer {

    private ArrayList<Habit> habits;
    private DataSource<Habit> habitDataSource;
    private String userId;

    private ListView habitsListView;
    private MyHabitsAdapter habitsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Set up the list view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_habits, container, false);

        userId = CurrentUser.getInstance().getUserId();

        habitDataSource = new HabitDataSource(userId);
        habitDataSource.addObserver(this);
        habits = habitDataSource.getSource();

        HabitSummaryActivity habitSummaryActivity = (HabitSummaryActivity) getActivity();
        habitsListView = (ListView) view.findViewById(R.id.myHabitsListView);
        habitsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                viewHabit(position);
            }
        });
        habitsAdapter = new MyHabitsAdapter(getActivity(), habits);
        habitsListView.setAdapter(habitsAdapter);

        return view;
    }

    /**
     * Update the list view
     */
    @Override
    public void update(Observable observable, Object o) {
        if(habitsAdapter != null) {
            habitsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Navigate to the View Habit Activity when the user presses a habit
     * @param position position of selected habit in ListView
     *
     * @see cmput301f17t13.com.catisadog.activities.summary.ViewHabitActivity
     */

    public void viewHabit(int position) {
        Habit viewHabit = habitsAdapter.getItem(position);

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.VIEW_HABIT_HABIT_KEY), viewHabit.getKey());
        bundle.putString(getString(R.string.VIEW_HABIT_USER_ID), viewHabit.getUserId());

        Intent intent = new Intent(getActivity(), ViewHabitActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * An adapter for converting habit objects into "my habits" to be displayed in a list
     * view.
     */
    private class MyHabitsAdapter extends ArrayAdapter<Habit> {
        public MyHabitsAdapter(Context context, ArrayList<Habit> habits) {
            super(context, 0, habits);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Habit habit = this.getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_my_habit, parent, false);
            }

            TextView titleView = (TextView) convertView.findViewById(R.id.myHabitListItemTitle);
            TextView reasonView = (TextView) convertView.findViewById(R.id.myHabitListItemReason);
            TextView startDateView = (TextView) convertView.findViewById(R.id.myHabitListItemStartDate);
            titleView.setText(habit.getTitle());
            reasonView.setText(habit.getReason());
            startDateView.setText(habit.getStartDate().toString("d MMM"));

            return convertView;
        }
    }

}
