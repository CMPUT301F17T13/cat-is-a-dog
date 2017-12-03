package cmput301f17t13.com.catisadog.utils.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;

public class DateUtil {
    public static ArrayList<Week>
            GetNPastWeeks(DateTime dateTime, int nPastWeeks) {
        if (nPastWeeks < 1) return new ArrayList<>();
        LocalDate localDate = new LocalDate(dateTime);
        DateTime monday = localDate.withDayOfWeek(DateTimeConstants.MONDAY)
                                    .toDateTimeAtStartOfDay();
        DateTime sunday = localDate.withDayOfWeek(DateTimeConstants.SUNDAY)
                                   .toDateTimeAtStartOfDay().plusDays(1)
                                   .minusMillis(1);
        Week datePair = new Week(monday, sunday);
        ArrayList<Week> datePairs = new ArrayList<>(nPastWeeks);
        datePairs.add(datePair);
        for (int i = 1; i < nPastWeeks; ++i) {
            datePairs.add(new Week(
                    datePairs.get(i - 1).getStartOfWeek().minusWeeks(1),
                    datePairs.get(i - 1).getEndOfWeek().minusWeeks(1)));
        }
        Collections.reverse(datePairs);
        return datePairs;
    }

    public static int WeekDifference(DateTime startDate, DateTime endDate) {
        Duration d = new Duration(startDate.toInstant(), endDate.toInstant());

        return (int)(d.getStandardDays() / 7);
    }
}
