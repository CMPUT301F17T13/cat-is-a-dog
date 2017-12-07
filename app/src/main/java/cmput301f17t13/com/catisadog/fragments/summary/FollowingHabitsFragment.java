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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.Observer;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;
import cmput301f17t13.com.catisadog.models.HabitStatus;
import cmput301f17t13.com.catisadog.models.followrequest.FollowRequestDataSource;
import cmput301f17t13.com.catisadog.models.followrequest.SocialDataSource;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.HabitDataSource;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.models.user.User;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.Repository;

/**
 * A screen for showing habitEvents for all users the current user is following
 * Child Fragment of HabitSummaryActivity
 * @see HabitSummaryActivity
 */

public class FollowingHabitsFragment extends Fragment
    implements Observer {

    private DataSource<User> followingDataSource;
    private ArrayList<User> following = new ArrayList<>();
    private LinkedHashMap<String, ArrayList<Habit>> followingHabitMap = new LinkedHashMap<>();
    private BaseExpandableListAdapter followingHabitsAdapter;

    private ArrayList<DataSource> registeredSources;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        following.add(CurrentUser.getInstance());
        registeredSources = new ArrayList<>();
    }

    /**
     * Instantiate our data sources with firebase and our list adapters / view bindings
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following_habits, container, false);

        ExpandableListView followingHabitsView =
                (ExpandableListView) view.findViewById(R.id.following_habits_list);

        String userId = CurrentUser.getInstance().getUserId();

        followingDataSource = new SocialDataSource(userId, SocialDataSource.UserType.FOLLOWING);
        followingDataSource.addObserver(this);
        following = followingDataSource.getSource();

        followingHabitsAdapter = new FollowingHabitsListAdapter(
                getActivity(),
                following,
                followingHabitMap
                );

        followingHabitsView.setAdapter(followingHabitsAdapter);

        return view;
    }

    /**
     * Setup the firebase connections
     */
    @Override
    public void onResume() {
        super.onResume();
        followingDataSource.open();
        for (DataSource source : registeredSources) {
            source.open();
        }
    }

    /**
     * Cleanup the firebase connections
     */
    @Override
    public void onPause() {
        super.onPause();
        followingDataSource.close();
        for (DataSource source : registeredSources) {
            source.close();
        }
    }

    /**
     * Update the lists with new firebase data. If the updating observable is the {@link SocialDataSource}
     * that means that we are adding a new user to the list and we must fetch that user's habits.
     * If otherwise the updating observable is a child {@link Habit} that we add to the appropriate
     * following user. Once we add the new data we update the list in a streaming fashion.
     * @param observable either a {@link SocialDataSource} or a {@link HabitDataSource}
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof SocialDataSource) {
            User newFollowing = (User) o;
            DataSource<Habit> followingHabitSource = new HabitDataSource(newFollowing.getUserId());
            ArrayList<Habit> followingHabits = followingHabitSource.getSource();
            followingHabitSource.addObserver(this);
            followingHabitSource.open();
            registeredSources.add(followingHabitSource);
            followingHabitMap.put(newFollowing.getUserId(), followingHabits);
        }
        else {
            String userId = (String) o;
            ArrayList<Habit> habitArray = followingHabitMap.get(userId);
            if (habitArray != null) {
                Collections.sort(habitArray, new Comparator<Habit>() {
                    @Override
                    public int compare(Habit one, Habit two) {
                        return one.getTitle().compareTo(two.getTitle());
                    }
                });
            }
        }

        followingHabitsAdapter.notifyDataSetChanged();
    }
}
