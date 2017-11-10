/*
 * Following Habits Fragment
 *
 * Copyright (c) 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.fragments.summary;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;

/**
 * A screen for showing habits for all users the current user is following
 * Child Fragment of HabitSummaryActivity
 *
 * @see HabitSummaryActivity
 */

public class FollowingHabitsFragment extends Fragment {

    public FollowingHabitsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_following_habits, container, false);
    }

    /**
     * Navigate to the View Habit Activity when the user presses a habit
     * @param v the button view
     *
     * @see cmput301f17t13.com.catisadog.activities.summary.ViewHabitActivity
     */

    public void viewHabit(View v) {

    }

}
