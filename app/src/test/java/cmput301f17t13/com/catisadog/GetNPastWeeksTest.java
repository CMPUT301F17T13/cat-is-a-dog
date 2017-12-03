package cmput301f17t13.com.catisadog;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.Assert;

import java .util.ArrayList;

import cmput301f17t13.com.catisadog.utils.date.Week;
import cmput301f17t13.com.catisadog.utils.date.DateUtil;

public class GetNPastWeeksTest {
    @Test
    public void testEmpty() {
        ArrayList<Week> result;
        result = DateUtil.GetNPastWeeks(DateTime.now(), 0);
        Assert.assertEquals(0, result.size());
        result = DateUtil.GetNPastWeeks(DateTime.now(), -1);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testOne() {
        DateTime date = new DateTime(2017, 12, 5, 12, 34, 0);
        DateTime startOfWeek = new DateTime(2017, 12, 4, 0, 0, 0);
        DateTime endOfWeek = new DateTime(2017, 12, 10, 23, 59, 59, 999);
        ArrayList<Week> result = DateUtil.GetNPastWeeks(date, 1);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(startOfWeek, result.get(0).getStartOfWeek());
        Assert.assertEquals(endOfWeek, result.get(0).getEndOfWeek());
    }

    @Test
    public void testMany() {
        DateTime date = new DateTime(2017, 12, 5, 12, 34, 0);
        DateTime startOfWeek = new DateTime(2017, 12, 4, 0, 0, 0);
        DateTime endOfWeek = new DateTime(2017, 12, 10, 23, 59, 59, 999);
        DateTime startOfLastWeek = new DateTime(2017, 11, 27, 0, 0, 0);
        DateTime endOfLastWeek = new DateTime(2017, 12, 3, 23, 59, 59, 999);
        ArrayList<Week> result = DateUtil.GetNPastWeeks(date, 2);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(startOfLastWeek, result.get(0).getStartOfWeek());
        Assert.assertEquals(endOfLastWeek, result.get(0).getEndOfWeek());
        Assert.assertEquals(startOfWeek, result.get(1).getStartOfWeek());
        Assert.assertEquals(endOfWeek, result.get(1).getEndOfWeek());
    }
}
