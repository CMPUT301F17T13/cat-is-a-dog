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

        private static final int TODO_TYPE = 0;
        private static final int COMPLETE_TYPE = 1;

        private LayoutInflater mInflater;

        public TodoHabitsAdapter(Context context, ArrayList<Habit> habits) {
            super(context, 0, habits);

            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder = null;
            Habit habit = this.getItem(position);
            int type = habit.isComplete() ? COMPLETE_TYPE : TODO_TYPE;

            if(convertView == null || ((ViewHolder) convertView.getTag()).type != type) {
                holder = new ViewHolder();
                switch (type) {
                    case TODO_TYPE:
                        convertView = mInflater.inflate(R.layout.item_todo_habit, parent, false);

                        holder.type = TODO_TYPE;
                        holder.titleView = (TextView) convertView.findViewById(R.id.todoHabitListItemTitle);
                        holder.reasonView = (TextView) convertView.findViewById(R.id.todoHabitListItemReason);
                        holder.startDateView = (TextView) convertView.findViewById(R.id.todoHabitListItemStartDate);
                        holder.addEventButton = (Button) convertView.findViewById(R.id.todoHabitAddEvent);
                        break;
                    case COMPLETE_TYPE:
                        convertView = mInflater.inflate(R.layout.item_complete_habit, parent, false);

                        holder.type = COMPLETE_TYPE;
                        holder.titleView = (TextView) convertView.findViewById(R.id.todoHabitCompleteTitle);
                        break;
                }
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            switch (type) {
                case TODO_TYPE:
                    holder.titleView.setText(habit.getTitle());
                    holder.reasonView.setText(habit.getReason());
                    holder.startDateView.setText(habit.getStartDate().toString("d MMM"));
                    holder.addEventButton.setOnClickListener(getAddEventOnClickListener(habit));
                    break;
                case COMPLETE_TYPE:
                    holder.titleView.setText(habit.getTitle());
                    break;
            }

            return convertView;
        }

        private View.OnClickListener getAddEventOnClickListener(final Habit habit) {
            return new View.OnClickListener() {
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
        }
    }

    public static class ViewHolder {
        public int type;
        public TextView titleView;
        public TextView reasonView;
        public TextView startDateView;
        public Button addEventButton;
    }
}
