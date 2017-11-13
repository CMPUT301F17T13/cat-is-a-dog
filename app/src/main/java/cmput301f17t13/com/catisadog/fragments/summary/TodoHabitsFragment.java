/*
 * TodoHabits Fragment
 *
 * Copyright (c) 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.fragments.summary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;
import cmput301f17t13.com.catisadog.models.Habit;

/**
 * A screen for seeing habits scheduled for today
 *
 *
 */

public class TodoHabitsFragment extends Fragment {

    private ListView habitsListView;
    private TodoHabitsAdapter habitsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_habits, container, false);
        habitsListView = (ListView) view.findViewById(R.id.todoHabitsListView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        HabitSummaryActivity habitSummaryActivity = (HabitSummaryActivity) getActivity();
        habitsAdapter = new TodoHabitsAdapter(habitSummaryActivity, habitSummaryActivity.todoHabits);
        habitsListView.setAdapter(habitsAdapter);
    }

    public void updateListView() {
        if (habitsAdapter != null) {
            habitsAdapter.notifyDataSetChanged();
        }
    }

    private class TodoHabitsAdapter extends ArrayAdapter<Habit> {
        public TodoHabitsAdapter(Context context, ArrayList<Habit> habits) {
            super(context, 0, habits);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Habit habit = this.getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo_habit, parent, false);
            }

            TextView titleView = (TextView) convertView.findViewById(R.id.todoHabitListItemTitle);
            TextView reasonView = (TextView) convertView.findViewById(R.id.todoHabitListItemReason);
            TextView startDateView = (TextView) convertView.findViewById(R.id.todoHabitListItemStartDate);
            Button addHabitEventButton = (Button) convertView.findViewById(R.id.todoHabitAddEvent);
            titleView.setText(habit.getTitle());
            reasonView.setText(habit.getReason());
            startDateView.setText(habit.getStartDate().toString("d MMM"));

            View.OnClickListener addHabitEventButtonListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Event", "Add habit event");
                }
            };

            addHabitEventButton.setOnClickListener(addHabitEventButtonListener);

            return convertView;
        }
    }

}
