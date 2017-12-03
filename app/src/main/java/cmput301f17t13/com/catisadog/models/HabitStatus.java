package cmput301f17t13.com.catisadog.models;

import android.graphics.Color;

import java.io.Serializable;

public class HabitStatus implements Serializable {
    public static final HabitStatus ON_TRACK = new HabitStatus("On track", Color.GREEN);
    public static final HabitStatus NEAR_GOAL = new HabitStatus("Near goal", Color.rgb(240, 120, 0));
    public static final HabitStatus INACTIVE = new HabitStatus("Inactive", Color.RED);

    private String message;
    private int color;

    private HabitStatus(String message, int color) {
        this.message = message;
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public int getColor() { return color; }

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
