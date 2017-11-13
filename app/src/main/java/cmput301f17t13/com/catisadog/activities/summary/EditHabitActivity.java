/*
 * Edit Habit Activity
 *
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.activities.summary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.models.Habit;
import cmput301f17t13.com.catisadog.utils.IntentConstants;


public class EditHabitActivity extends Activity {
    private Habit habit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        habit = (Habit) bundle.getSerializable(
                IntentConstants.EDIT_HABIT_INTENT_DATA);
//        setContentView(R.layout.activity_edit_habit);
    }

    /**
     * Save changes to the habit
     * @param v the button view
     */
    public void saveChanges(View v) {


    }
}
