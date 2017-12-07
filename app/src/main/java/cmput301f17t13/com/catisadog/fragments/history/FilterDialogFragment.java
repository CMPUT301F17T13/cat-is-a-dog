package cmput301f17t13.com.catisadog.fragments.history;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import cmput301f17t13.com.catisadog.models.habit.Habit;
import cmput301f17t13.com.catisadog.activities.history.HabitHistoryActivity;

/**
 * The dialog that pops up to filter the the habit history when you press the filter button
 * @see HabitHistoryActivity
 */
public class FilterDialogFragment extends DialogFragment {
    public enum FilterType {
        SEARCH_BY_HABIT,
        SEARCH_BY_COMMENT,
        NEAR_LOCATION,
        MY_RECENT_EVENTS,
        FRIENDS_RECENT_EVENTS,
    }

    private FilterDialogResultListener listener;

    private static final CharSequence[] filterOptions = {
            "Search by Habit", "Search by Comment",
            "Within 5 km of my location", "My recent events",
            "Friends' recent events" };

    /**
     *
     * @param listener the of the parent activity which will receive our selection
     * @see HabitHistoryActivity
     */
    public void setResultListener(FilterDialogResultListener listener) {
        this.listener = listener;
    }

    /**
     * Set the parent activity as the listener of this dialog's result
     * @param context the context of the parent activity
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FilterDialogResultListener) {
            listener = (FilterDialogResultListener) context;
        }
    }

    /**
     * Instantiate the dialog and populate the options plus event handler.
     */
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Filter Habit Events")
            .setItems(filterOptions, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: // Search by habit
                            createHabitFilterDialog(getArguments()).show();
                            break;
                        case 1: // Search by comment
                            createCommentFilterDialog().show();
                            break;
                        case 2: // Near location
                            listener.filterResult(FilterType.NEAR_LOCATION, "");
                            break;
                        case 3:
                            listener.filterResult(FilterType.MY_RECENT_EVENTS, "");
                            break;
                        case 4: // Friends' recent events
                            listener.filterResult(
                                    FilterType.FRIENDS_RECENT_EVENTS, "");
                            break;
                        default:
                            break;
                    }
                }
            });
        return builder.create();
    }

    /**
     * Creates the sub-dialogue where you can choose from a list of the current user's active habits
     */
    private Dialog createHabitFilterDialog(Bundle savedInstanceState) {
        final ArrayList<Habit> habits =
                (ArrayList<Habit>) savedInstanceState.getSerializable("habits");
        CharSequence[] habitOptions = new CharSequence[habits.size()];
        for (int i = 0; i < habits.size(); ++i) {
            habitOptions[i] = habits.get(i).getTitle();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Filter habit events")
            .setItems(habitOptions, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    listener.filterResult(FilterType.SEARCH_BY_HABIT,
                                          habits.get(which).getKey());
                }
            });
        return builder.create();
    }

    /**
     * Creates the sub-dialogue where you can enter the substring to search for
     */
    private Dialog createCommentFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(layoutParams);
        builder.setTitle("Filter habit events")
                .setView(input)
                .setPositiveButton("Search",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.filterResult(FilterType.SEARCH_BY_COMMENT,
                                input.getText().toString());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,
                                        int i) {}
                });
        return builder.create();
    }

    /**
     * Interface to implement for objects that wish to listen on the result of this dialog
     */
    public interface FilterDialogResultListener {
        void filterResult(FilterType filterType, String filterData);
    }
}
