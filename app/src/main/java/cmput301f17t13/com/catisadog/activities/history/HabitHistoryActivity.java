package cmput301f17t13.com.catisadog.activities.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;

public class HabitHistoryActivity extends AppCompatActivity {
    private HabitHistoryActivity.HabitHistoryAdapter habitsAdapter;
    
    public ArrayList<HabitEvent> habitEvents;

    private @BindView(R.id.list) ListView habitsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);
        ButterKnife.bind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        habitsAdapter = new HabitHistoryActivity.HabitHistoryAdapter(this, habitEvents);
        habitsListView.setAdapter(habitsAdapter);
    }

    public void updateListView() {
        if (habitsAdapter != null) {
            habitsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * An adapter for converting habit objects into to-do habitEvents to be displayed in a list
     * view.
     */
    private class HabitHistoryAdapter extends ArrayAdapter<HabitEvent> {
        public HabitHistoryAdapter(Context context, ArrayList<HabitEvent> habits) {
            super(context, 0, habits);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final HabitEvent habitEvent = this.getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_my_habit_events, parent, false);
            }

            TextView titleView = (TextView) convertView.findViewById(R.id.myHabitEventListItemTitle);
            TextView reasonView = (TextView) convertView.findViewById(R.id.myHabitEventListItemReason);
            TextView startDateView = (TextView) convertView.findViewById(R.id.myHabitEventListItemStartDate);
            /*titleView.setText(habitEvent.getHabit());
            reasonView.setText(habitEvent.getReason());
            startDateView.setText(habitEvent.getStartDate().toString("d MMM"));*/

            /*View.OnClickListener addHabitEventButtonListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Event", "Add habit event");
                    Intent intent = new Intent(getActivity(), AddHabitEventActivity.class);
                    Bundle b = new Bundle();
                    b.putString(IntentConstants.ADD_HABIT_EVENT_INTENT_DATA, habit.getKey());
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                }
            };

            addHabitEventButton.setOnClickListener(addHabitEventButtonListener);*/

            return convertView;
        }
    }
}
