/*
 * View Habit Activity
 *
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman,
 * Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable
 * law or agreed to in writing, software.
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.activities.summary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.constraint.Group;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Set;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.HabitDataModel;
import cmput301f17t13.com.catisadog.models.habit.HabitDataSource;
import cmput301f17t13.com.catisadog.models.habit.HabitRepository;
import cmput301f17t13.com.catisadog.utils.IntentConstants;
import cmput301f17t13.com.catisadog.utils.data.Repository;

/**
 * A screen for viewing habit details
 *
 * @see Habit
 */
public class ViewHabitActivity extends AppCompatActivity {

    private static final String TAG = "ViewHabitActivity";
    private static final int DELETE_BUTTON_ID = 1032400432;

    private String habitOwner;
    private String habitKey;
    private Habit habit;
    private Repository<Habit> habitRepository;

    private Group mHabitViewGroup;
    private ProgressBar mLoadingBar;

    /**
     * OnCreate method to setup the UI and fill TextView values with habit data
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        habitKey = bundle.getString(getString(R.string.VIEW_HABIT_HABIT_KEY), "invalid");
        habitOwner = bundle.getString(getString(R.string.VIEW_HABIT_USER_ID), "invalid");

        mHabitViewGroup = (Group) findViewById(R.id.habitViewGroup);
        mLoadingBar = (ProgressBar) findViewById(R.id.habitViewLoadingBar);

        habitRepository = new HabitRepository(habitOwner);
    }

    /**
     * Pull the latest version of the habit and reflect it on the UI
     * If it doesn't exist, close the activity
     */
    @Override
    protected void onStart() {
        super.onStart();

        mHabitViewGroup.setVisibility(View.GONE);
        mLoadingBar.setVisibility(View.VISIBLE);

        DatabaseReference habitRef = FirebaseDatabase.getInstance().getReference("habitEvents/" + habitOwner + "/" + habitKey);
        habitRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HabitDataModel model = dataSnapshot.getValue(HabitDataModel.class);
                if (model != null) {
                    habit = model.getHabit();
                    updateUI();
                }
                else {
                    Toast.makeText(ViewHabitActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                finish();
            }
        });
    }

    /**
     * Update the UI to reflect the current habit
     */
    private void updateUI() {
        if (habit != null) {
            ((TextView) findViewById(R.id.habitTitle)).setText(habit.getTitle());
            ((TextView) findViewById(R.id.habitReason)).setText(habit.getReason());
            ((TextView) findViewById(R.id.habitStartValue)).setText(startsText());
            ((TextView) findViewById(R.id.habitRepeatsValue))
                    .setText(repeatsText());
            ((TextView) findViewById(R.id.habitCompletionMetricsValue))
                    .setText(completionMetricsText());

            mLoadingBar.setVisibility(View.GONE);
            mHabitViewGroup.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Create menu items such as "delete" button
     * @param menu The menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem deleteItem = menu.add(
                Menu.NONE, DELETE_BUTTON_ID, Menu.NONE, "Delete");
        MenuItemCompat.setShowAsAction(
                deleteItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);
        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                confirmDelete();
                return true;
            }
        });
        return true;
    }

    /**
     * Confirm before deleting habit
     */
    private void confirmDelete() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmation");
        alert.setMessage("Are you sure you would like to delete the habit and all its habit events?");
        alert.setPositiveButton("Delete habit", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                habitRepository.delete(habit.getKey());
                dialog.dismiss();
                finish();
            }
        });
        alert.setNegativeButton("Cancel", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    /**
     * Navigate to edit the habit if it belongs to the current user
     * @param v the button view
     */
    public void editHabit(View v) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstants.EDIT_HABIT_INTENT_DATA, habit);
        Intent intent = new Intent(this, EditHabitActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Get formatted string for start text TextView
     * @return String representation of start text
     */
    private String startsText() {
        return habit.getStartDate().toString("EEEE MMMM dd, yyyy");
    }

    /**
     * Get formatted string for repeats TextView
     * Speial formatting if every day, weekends, or weekdays are selected
     * @return String representation of repeat text
     */
    private String repeatsText() {
        Set<Integer> schedule = habit.getSchedule();
        if (schedule.isEmpty()) {
            return "Never";
        } else if (schedule.size() == 7) {
            return "Every day";
        } else if (schedule.size() == 5 &&
                schedule.contains(DateTimeConstants.MONDAY) &&
                schedule.contains(DateTimeConstants.TUESDAY) &&
                schedule.contains(DateTimeConstants.WEDNESDAY) &&
                schedule.contains(DateTimeConstants.THURSDAY) &&
                schedule.contains(DateTimeConstants.FRIDAY)) {
            return "Weekdays";
        } else if (schedule.size() == 2 &&
                schedule.contains(DateTimeConstants.SATURDAY) &&
                schedule.contains(DateTimeConstants.SUNDAY)) {
            return "Weekends";
        } else {
            ArrayList<String> dates = new ArrayList<>();
            if (schedule.contains(DateTimeConstants.SUNDAY)) {
                dates.add("Sun");
            }
            if (schedule.contains(DateTimeConstants.MONDAY)) {
                dates.add("Mon");
            }
            if (schedule.contains(DateTimeConstants.TUESDAY)) {
                dates.add("Tue");
            }
            if (schedule.contains(DateTimeConstants.WEDNESDAY)) {
                dates.add("Wed");
            }
            if (schedule.contains(DateTimeConstants.THURSDAY)) {
                dates.add("Thu");
            }
            if (schedule.contains(DateTimeConstants.FRIDAY)) {
                dates.add("Fri");
            }
            if (schedule.contains(DateTimeConstants.SATURDAY)) {
                dates.add("Sat");
            }
            return TextUtils.join(", ", dates);
        }
    }

    /**
     * Completion metrics text
     * TODO(#47): Return string representation of habitEvents when complete
     * @return String representing completion metrics
     */
    private String completionMetricsText() {
        return "Coming soon!\n" +
                "Past 7 occurences:\n" +
                "Past month:\n" +
                "All time:";
    }
}
