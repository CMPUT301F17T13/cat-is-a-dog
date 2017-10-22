package cmput301f17t13.com.catisadog.models;

import android.graphics.drawable.Drawable;

public class HabitStatus {

    private String message;
    private Drawable icon; //Resource key

    public HabitStatus(String message, Drawable icon) {
        this.message = message;
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
