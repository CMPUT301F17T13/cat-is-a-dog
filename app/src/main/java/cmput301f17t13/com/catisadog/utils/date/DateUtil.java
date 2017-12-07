package cmput301f17t13.com.catisadog.utils.date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class DateUtil {
    /**
     * Generates the datetime bounds for the last N weeks, this week inclusive
     * The first day of the week is considered to be a Monday (1)
     * @param dateTime the anchor date
     * @param nPastWeeks the number of weeks to compute
     * @return a series of n week objects, containing the start date and end date of those weeks
     */
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

    /**
     * Calculates the week difference between two dates, rounded down to the nearest integer
     * @param startDate the first date
     * @param endDate the second date
     * @return the difference in weeks between the dates
     */
    public static int WeekDifference(DateTime startDate, DateTime endDate) {
        Duration d = new Duration(startDate.toInstant(), endDate.toInstant());

        return (int)(d.getStandardDays() / 7);
    }

    /**
     * Return the current day of week as an integer between 1 and 7
     * @param dateTime the date
     * @return the day of week of date
     */
    public static int GetDayOfWeek(DateTime dateTime) {
        return (dateTime.toGregorianCalendar().get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1;
    }
}
