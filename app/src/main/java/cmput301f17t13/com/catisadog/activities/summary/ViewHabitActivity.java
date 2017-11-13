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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.utils.IntentConstants;

/**
 * A screen for viewing habit details
 *
 * @see cmput301f17t13.com.catisadog.models.Habit
 */
public class ViewHabitActivity extends AppCompatActivity {
    private Habit habit;

    /**
     * OnCreate method to setup the UI and fill TextView values with habit data
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        //TODO(#51): Get Habit object from Repository, not intent
        Bundle bundle = intent.getExtras();
        habit = (Habit) bundle.getSerializable(
                IntentConstants.VIEW_HABIT_INTENT_DATA);
        setContentView(R.layout.activity_view_habit);
        ((TextView) findViewById(R.id.habitTitle)).setText(habit.getTitle());
        ((TextView) findViewById(R.id.habitReason)).setText(habit.getReason());
        ((TextView) findViewById(R.id.habitStartValue)).setText(startsText());
        ((TextView) findViewById(R.id.habitRepeatsValue))
                .setText(repeatsText());
        ((TextView) findViewById(R.id.habitCompletionMetricsValue))
                .setText(completionMetricsText());
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
     * TODO(#47): Return string representation of habits when complete
     * @return String representing completion metrics
     */
    private String completionMetricsText() {
        return "Coming soon!\n" +
                "Past 7 occurences:\n" +
                "Past month:\n" +
                "All time:";
    }
}
