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
import android.graphics.Color;
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
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.models.habit.CompletionMetricDataSource;
import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.models.habit.HabitDataModel;
import cmput301f17t13.com.catisadog.models.habit.HabitDataSource;
import cmput301f17t13.com.catisadog.models.habit.HabitRepository;
import cmput301f17t13.com.catisadog.utils.IntentConstants;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.OnResultListener;
import cmput301f17t13.com.catisadog.utils.data.Repository;
import cmput301f17t13.com.catisadog.utils.date.DateUtil;

/**
 * A screen for viewing habit details
 *
 * @see Habit
 */
public class ViewHabitActivity extends AppCompatActivity
    implements Observer {

    private static final String TAG = "ViewHabitActivity";
    private static final int DELETE_BUTTON_ID = 1032400432;

    private String habitOwner;
    private String habitKey;
    private Habit habit;
    private Repository<Habit> habitRepository;
    private ArrayList<Double> completionMetrics;

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

        habitRepository.get(habitKey, new OnResultListener<Habit>() {
            @Override
            public void onResult(Habit h) {
                if (h != null) {
                    habit = h;
                    getCompletionData();
                    updateUI();
                }
                else {
                    Toast.makeText(ViewHabitActivity.this,
                            "Something went wrong.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    /**
     * Creates the completion metrics graph for the habit
     */
    private void createCompletionGraph() {

        int startedWeeksAgo = DateUtil.WeekDifference(habit.getStartDate(), DateTime.now()) + 1;
        if (startedWeeksAgo > 6) startedWeeksAgo = 6;

        Double runningTotal = 0.00;
        Double[] runningAverages = new Double[7];
        for (int i = -1 * startedWeeksAgo; i <= 0; i++) {
            runningTotal += completionMetrics.get(6+i);
            runningAverages[6+i] = runningTotal/(startedWeeksAgo + i + 1);
        }

        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        for (int i = -1 * startedWeeksAgo; i <= 0; i++) {
            if(runningAverages[6+i] == null) runningAverages[6+i] = 0.00;
            dataPoints.add(new DataPoint(i, runningAverages[6+i]));
        }

        habit.setCompletionRate(runningAverages[6]);
        habitRepository.update(habit.getKey(), habit);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints.toArray(new DataPoint[dataPoints.size()]));

        series.setTitle("Completion Metrics");
        series.setColor(Color.RED);

        graph.getViewport().setMinX(-7);
        graph.getViewport().setMaxX(0);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);

        // custom label formatter to show percentages
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " %";
                }
            }
        });

        graph.addSeries(series);
    }

    /**
     * Fetches the completion data for the habit
     */
    private void getCompletionData() {
        DataSource<Double> metricSource = new CompletionMetricDataSource(habit);
        completionMetrics = metricSource.getSource();
        metricSource.addObserver(this);
        //return new double[] { 95.0,90.1,70.5,50.2,35.1, 35.1, 30 };
    }

    @Override
    public void update(Observable o, Object arg) {
        createCompletionGraph();
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

}
