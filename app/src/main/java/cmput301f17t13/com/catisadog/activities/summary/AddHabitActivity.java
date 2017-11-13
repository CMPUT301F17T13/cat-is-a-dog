/*
 * Add Habit Fragment Activity
 *
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

/**
 * A screen for creating habits. Child of Habit Summary Activity
 * Child Fragment Activity of HabitSummaryActivity
 *
 * @see HabitSummaryActivity
 * @see cmput301f17t13.com.catisadog.models.Habit
 */

package cmput301f17t13.com.catisadog.activities.summary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.UUID;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.utils.IntentConstants;

public class AddHabitActivity extends AppCompatActivity {

    private static final String TAG = "AddHabitActivity";

    private TextView startDateView;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private WeekdaysDataSource.Callback wdsCallback;
    private DateTime startDate;
    private WeekdaysDataSource wds;

    /**
     * Set up calendar and weekday picker widgets
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        startDateView = (TextView) findViewById(R.id.addHabitStartDate);
        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddHabitActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        startDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                startDate = new DateTime(year, month, day, 0 ,0);
                startDateView.setText(startDate.toString("EEEE MMMM d, YYYY"));
            }
        };

        wdsCallback = new WeekdaysDataSource.Callback() {
            @Override
            public void onWeekdaysItemClicked(int i, WeekdaysDataItem weekdaysDataItem) {}

            @Override
            public void onWeekdaysSelected(int i, ArrayList<WeekdaysDataItem> arrayList) {
            }
        };


        wds = new WeekdaysDataSource(this, R.id.addHabitRepeatWeekdaysStub).start(wdsCallback);
    }

    /**
     * Save habit to current user
     * @param v button view
     */
    public void saveHabit(View v) {
        String habitTitle = ((EditText) findViewById(R.id.addHabitTitle)).getText().toString();
        String habitReason = ((EditText) findViewById(R.id.addHabitReason)).getText().toString();

        if (habitTitle.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDate == null) {
            Toast.makeText(getApplicationContext(), "Please enter a starting date", Toast.LENGTH_SHORT).show();
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

        Habit habit = new Habit(CurrentUser.getInstance().getUserId(), habitTitle, habitReason, startDate, schedule, null);

        Intent intent = new Intent();
        intent.putExtra(IntentConstants.ADD_HABIT_INTENT_DATA, habit);
        setResult(IntentConstants.ADD_HABIT_INTENT_RESULT, intent);
        finish();
    }
}
