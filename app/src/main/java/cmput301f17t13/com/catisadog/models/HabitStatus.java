package cmput301f17t13.com.catisadog.models;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;

public class HabitStatus {
    public static final HabitStatus ON_TRACK = new HabitStatus("On track", new DrawableContainer());

    private String message;
    private Drawable icon; //Resource key

    private HabitStatus(String message, Drawable icon) {
        this.message = message;
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getMessage() {
        return message;
    }

}
