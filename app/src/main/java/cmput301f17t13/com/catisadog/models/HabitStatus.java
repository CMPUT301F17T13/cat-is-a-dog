package cmput301f17t13.com.catisadog.models;

import android.graphics.Color;

import java.io.Serializable;

public class HabitStatus implements Serializable {
    // ON_TRACK means over 85% completion
    public static final HabitStatus ON_TRACK = new HabitStatus("On track", Color.GREEN);
    // NEAR_GOAL means over 40-80% completion
    public static final HabitStatus NEAR_GOAL = new HabitStatus("Near goal", Color.rgb(240, 120, 0));
    // INACTIVE means less than 40% completion
    public static final HabitStatus INACTIVE = new HabitStatus("Inactive", Color.RED);

    private String message;
    private int color;

    private HabitStatus(String message, int color) {
        this.message = message;
        this.color = color;
    }

    /**
     * Get the message associated to the status
     * @return the status message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the color associated to the status
     * @return the status color
     */
    public int getColor() { return color; }

    /**
     * Convert a completion rate to a corresponding status
     * @param completionRate the completion rate from 0 to 100 in percent
     * @return the corresponding habit status ON_TRACK to INACTIVE
     */
    public static HabitStatus fromCompletionRate(double completionRate) {
        if (completionRate > 85) {
            return HabitStatus.ON_TRACK;
        }
        else if (completionRate > 40) {
            return HabitStatus.NEAR_GOAL;
        }
        else {
            return HabitStatus.INACTIVE;
        }
    }
}
