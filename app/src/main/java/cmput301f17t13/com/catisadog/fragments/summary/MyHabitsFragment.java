/*
 * My Habits Fragment
 *
 * Copyright (c) 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.fragments.summary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cmput301f17t13.com.catisadog.R;

/**
 * A screen to view all the current users habits
 *
 * @see cmput301f17t13.com.catisadog.models.Habit
 */

public class MyHabitsFragment extends Fragment {


    public MyHabitsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_habits, container, false);
    }

    /**
     * Navigate to the View Habit Activity when the user presses a habit
     * @param v the button view
     *
     * @see cmput301f17t13.com.catisadog.activities.summary.ViewHabitActivity
     */

    public void viewHabit(View v) {

    }

    /**
     * Navigate to the Add Habit Activity when the user clicks the floating plus icon
     * @param v the button view
     */
    public void addHabit(View v) {

    }

}
