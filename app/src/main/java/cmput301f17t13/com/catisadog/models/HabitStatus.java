package cmput301f17t13.com.catisadog.models;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class HabitStatus {
    public static final HabitStatus ON_TRACK = new HabitStatus("On track", null, Color.GREEN);
    public static final HabitStatus NEAR_GOAL = new HabitStatus("Near goal", null, Color.YELLOW);
    public static final HabitStatus INACTIVE = new HabitStatus("Inactive", null, Color.RED);

    private String message;
    private int color;
    private Drawable icon; //Resource key

    private HabitStatus(String message, Drawable icon, int color) {
        this.message = message;
        this.icon = icon;
        this.color = color;
    }

    public Drawable getIcon() {
        return icon;
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
