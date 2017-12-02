/*
 * TodoHabits Fragment
 *
 * Copyright (c) 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.fragments.summary;

import android.content.Context;
import android.content.Intent;
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
import java.util.Observable;
import java.util.Observer;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.AddHabitEventActivity;
import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.TodoHabitDataSource;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.utils.IntentConstants;
import cmput301f17t13.com.catisadog.utils.data.DataSource;

/**
 * A screen for seeing habitEvents scheduled for today
 */

public class TodoHabitsFragment extends Fragment
        implements Observer {

    private String userId;
    private ArrayList<Habit> todoHabits;
    private DataSource<Habit> todoDataSource;

    private ListView habitsListView;
    private TodoHabitsAdapter todoHabitsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_habits, container, false);
        habitsListView = (ListView) view.findViewById(R.id.todoHabitsListView);

        userId = CurrentUser.getInstance().getUserId();

        todoDataSource = new TodoHabitDataSource(userId);
        todoDataSource.addObserver(this);
        todoHabits = todoDataSource.getSource();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        todoHabitsAdapter = new TodoHabitsAdapter(getActivity(), todoHabits);
        habitsListView.setAdapter(todoHabitsAdapter);
    }


    @Override
    public void update(Observable observable, Object o) {
        if (todoHabitsAdapter != null) {
            todoHabitsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * An adapter for converting habit objects into to-do habitEvents to be displayed in a list
     * view.
     */
    private class TodoHabitsAdapter extends ArrayAdapter<Habit> {
        public TodoHabitsAdapter(Context context, ArrayList<Habit> habits) {
            super(context, 0, habits);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Habit habit = this.getItem(position);

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
                    Intent intent = new Intent(getActivity(), AddHabitEventActivity.class);
                    Bundle b = new Bundle();
                    b.putString(IntentConstants.ADD_HABIT_EVENT_INTENT_DATA, habit.getKey());
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            };

            addHabitEventButton.setOnClickListener(addHabitEventButtonListener);

            return convertView;
        }
    }

}
