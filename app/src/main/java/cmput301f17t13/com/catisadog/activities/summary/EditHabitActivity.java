/*
 * Add Habit Fragment Activity
 *
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman,
 * Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable
 * law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

/**
 * A screen for creating habits. Child of Habit Summary Activity
 * Child Fragment Activity of HabitSummaryActivity
 *
 * @see HabitSummaryActivity
 * @see cmput301f17t13.com.catisadog.models.Habit
 */

package cmput301f17t13.com.catisadog.activities.summary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;
import com.touchboarder.weekdaysbuttons.WeekdaysDataSource;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.models.HabitDataSource;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.utils.IntentConstants;
import cmput301f17t13.com.catisadog.utils.data.Repository;

public class EditHabitActivity extends AppCompatActivity {

    private static final String TAG = "EditHabitActivity";
    private static final String DATE_FORMAT = "EEEE MMMM d, YYYY";
    private static final int SAVE_BUTTON_ID = 1032400432;

    private TextView editHabitStartDate;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private WeekdaysDataSource.Callback wdsCallback;
    private DateTime startDate;
    private WeekdaysDataSource wds;
    private Repository<Habit> habitRepository;

    private Boolean isNewHabit;
    private String habitKey;

    /**
     * Set up calendar and weekday picker widgets
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);
        editHabitStartDate = (TextView) findViewById(R.id.editHabitStartDate);
        editHabitStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        EditHabitActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        startDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month,
                                  int day) {
                month += 1;
                startDate = new DateTime(year, month, day, 0 ,0);
                editHabitStartDate.setText(startDate.toString(DATE_FORMAT));
            }
        };
        wdsCallback = new WeekdaysDataSource.Callback() {
            @Override
            public void onWeekdaysItemClicked(
                    int i, WeekdaysDataItem weekdaysDataItem) {}

            @Override
            public void onWeekdaysSelected(
                    int i, ArrayList<WeekdaysDataItem> arrayList) {}
        };
        wds = new WeekdaysDataSource(this,
                R.id.editHabitRepeatWeekdaysStub).start(wdsCallback);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle == null) {
            isNewHabit = false;
        }
        else {
            Habit habit = (Habit) bundle.getSerializable(
                    IntentConstants.EDIT_HABIT_INTENT_DATA);
            ((EditText) findViewById(R.id.editHabitTitle)).setText(
                    habit.getTitle());
            ((EditText) findViewById(R.id.editHabitReason)).setText(
                    habit.getReason());
            startDate = habit.getStartDate();
            editHabitStartDate.setText(startDate.toString(DATE_FORMAT));
            for (int day : habit.getSchedule()) {
                if (day == 7) day = 0;
                wds.setSelectedDays(day);
            }

            isNewHabit = true;
            habitKey = habit.getKey();
        }

        habitRepository = new HabitDataSource(CurrentUser.getInstance().getUserId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem deleteItem = menu.add(
                Menu.NONE, SAVE_BUTTON_ID, Menu.NONE, "Delete");
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

    private void confirmDelete() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alert!!");
        alert.setMessage("Are you sure to delete record");
        alert.setPositiveButton("Delete habit", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                habitRepository.delete(habitKey);
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
     * Save habit to current user
     * @param v button view
     */
    public void saveHabit(View v) {
        String habitTitle = ((EditText) findViewById(R.id.editHabitTitle))
                .getText().toString();
        String habitReason = ((EditText) findViewById(R.id.editHabitReason))
                .getText().toString();
        if (habitTitle.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (startDate == null) {
            Toast.makeText(getApplicationContext(),
                    "Please enter a starting date", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<WeekdaysDataItem> days = wds.getWeekdaysItems();
        HashSet<Integer> schedule = new HashSet<>();
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).isSelected()) {
                // Hack to map the weekdaybuttons values to DateTimeConstants
                if (i == 0) schedule.add(7);
                else schedule.add(i);
            }
        }

        Habit habit = new Habit(CurrentUser.getInstance().getUserId(),
                habitTitle, habitReason, startDate, schedule, null);
        habit.setKey(habitKey);

        if (isNewHabit) {
            habitRepository.update(habit.getKey(), habit);
        }
        else {
            habitRepository.add(habit);
        }

        finish();
    }
}
