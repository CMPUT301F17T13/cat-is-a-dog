/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.fragments.summary;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.user.User;
import cmput301f17t13.com.catisadog.utils.DownloadImageTask;

/**
 * Implementation of an expandable list adapter with linked hash map.
 * This expandable list is used for displaying the list of habits under each of the users you follow
 * @see FollowingHabitsFragment
 * @see HabitSummaryActivity
 */
public class FollowingHabitsListAdapter extends BaseExpandableListAdapter {

    private ArrayList<User> following;
    private LinkedHashMap<String, ArrayList<Habit>> habitMap;

    private Context mContext;
    private LayoutInflater mInflater;

    public FollowingHabitsListAdapter(
            Context context,
            ArrayList<User> following,
            LinkedHashMap<String, ArrayList<Habit>> habitMap) {
        this.mContext = context;
        this.following = following;
        this.habitMap = habitMap;

        mInflater = LayoutInflater.from(context);
    }

    /**
     * Create the main view group. This is where we display the followed user's photo, email and display name
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = (User) getGroup(groupPosition);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_user, parent, false);
        }

        TextView displayNameView = (TextView) convertView.findViewById(R.id.userDisplayName);
        TextView emailView = (TextView) convertView.findViewById(R.id.userEmail);
        ImageView photoView = (ImageView) convertView.findViewById(R.id.userPhoto);

        // Hide the button
        Button socialActionButton = (Button) convertView.findViewById(R.id.socialActionButton);
        socialActionButton.setVisibility(View.INVISIBLE);

        displayNameView.setText(user.getDisplayName());
        emailView.setText(user.getEmail());
        photoView.setTag(user.getPhotoUrl());
        new DownloadImageTask().execute(photoView);

        return convertView;
    }

    /**
     * Instantiate a child view containing the user you following's habit and their individual completion status
     * @param groupPosition
     * @param childPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Habit habit = (Habit) getChild(groupPosition, childPosition);

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_following_habit, parent, false);
        }

        TextView titleView = (TextView) convertView.findViewById(R.id.following_habit_title);
        titleView.setText(habit.getTitle());

        ImageView indicatorView = (ImageView) convertView.findViewById(R.id.following_habit_indicator);

        if (habit.getStatus() != null) {
            indicatorView.setColorFilter(habit.getStatus().getColor());
        }

        return convertView;
    }


    @Override
    public int getGroupCount() {
        return following.size();
    }

    /**
     * Get the number of habits for each following
     * @param groupPosition
     * @return the number of child habits
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        String userKey = following.get(groupPosition).getUserId();
        ArrayList<Habit> userHabits = habitMap.get(userKey);

        return userHabits != null ? userHabits.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return following.get(groupPosition);
    }

    /**
     * Get the habit children for a given child position and user position
     * @param groupPosition
     * @param childPosition
     * @return child habit object
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String userKey = following.get(groupPosition).getUserId();
        return habitMap.get(userKey).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
