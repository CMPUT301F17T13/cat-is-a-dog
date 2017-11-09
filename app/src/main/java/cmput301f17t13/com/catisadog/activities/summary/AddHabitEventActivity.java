package cmput301f17t13.com.catisadog.activities.summary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cmput301f17t13.com.catisadog.R;

/**
 * Screen for creating new habit events.
 * Child Activity of HabitSummaryActivity
 *
 * @see HabitSummaryActivity
 * @see cmput301f17t13.com.catisadog.models.HabitEvent
 */
public class AddHabitEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit_event);
    }
}
