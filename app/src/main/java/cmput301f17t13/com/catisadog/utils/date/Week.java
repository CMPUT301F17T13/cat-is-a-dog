package cmput301f17t13.com.catisadog.utils.date;

import org.joda.time.DateTime;

public class Week {
    private DateTime startOfWeek;
    private DateTime endOfWeek;

    public Week(DateTime startOfWeek, DateTime endOfWeek) {
        this.startOfWeek = startOfWeek;
        this.endOfWeek = endOfWeek;
    }

    public DateTime getStartOfWeek() {
        return startOfWeek;
    }

    public DateTime getEndOfWeek() {
        return endOfWeek;
    }
}
