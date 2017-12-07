package cmput301f17t13.com.catisadog.utils.date;

import org.joda.time.DateTime;

/**
 * Represents the start and end of one week
 */
public class Week {
    // The start date
    private DateTime startOfWeek;
    // The end date
    private DateTime endOfWeek;

    public Week(DateTime startOfWeek, DateTime endOfWeek) {
        this.startOfWeek = startOfWeek;
        this.endOfWeek = endOfWeek;
    }

    // Getters and setters

    /**
     * The first day of the week is considered to be a Monday
     * @return the start date of the week
     */
    public DateTime getStartOfWeek() {
        return startOfWeek;
    }

    /**
     * The last day of the week is considered to be a Sunday
     * @return the last day of the week
     */
    public DateTime getEndOfWeek() {
        return endOfWeek;
    }
}
