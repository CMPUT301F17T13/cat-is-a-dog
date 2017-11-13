/*
 * My Habits Fragment
 *
 * Copyright (c) 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.fragments.summary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.AddHabitActivity;
import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;
import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.utils.IntentConstants;

/**
 * A screen to view all the current users habits
 *
 * @see cmput301f17t13.com.catisadog.models.Habit
 */

public class MyHabitsFragment extends Fragment {

    private ListView habitsListView;
    private MyHabitsAdapter habitsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_habits, container, false);
        habitsListView = (ListView) view.findViewById(R.id.myHabitsListView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        HabitSummaryActivity habitSummaryActivity = (HabitSummaryActivity) getActivity();
        habitsAdapter = new MyHabitsAdapter(habitSummaryActivity, habitSummaryActivity.habits);
        habitsListView.setAdapter(habitsAdapter);
    }

    public void updateListView() {
        if(habitsAdapter != null) {
            habitsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Navigate to the View Habit Activity when the user presses a habit
     * @param v the button view
     *
     * @see cmput301f17t13.com.catisadog.activities.summary.ViewHabitActivity
     */

    public void viewHabit(View v) {

    }

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
