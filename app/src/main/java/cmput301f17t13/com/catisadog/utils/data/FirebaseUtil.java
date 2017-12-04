/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.utils.data;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.tz.ZoneInfoProvider;

import java.util.TimeZone;

public class FirebaseUtil {

    private final static char terminalCharacter = '\uf8ff';
    private final static String datePattern = "yyyy-MM-dd";
    private final static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(datePattern);

    private static DateTimeZone localTimeZone = DateTimeZone.forTimeZone(TimeZone.getDefault());

    // Use local time
    public static String dateToString(DateTime date) {
        date = date.toDateTime(localTimeZone);
        return dateFormatter.print(date.toInstant());
    }

    public static DateTime stringToDate(String dateString) {
        return dateFormatter.parseDateTime(dateString);
    }

    /**
     * Produces the range terminal key for a given key {@param key}
     * @param key the firebase key
     * @return the terminal key for a range matching {@param key}
     */
    public static String terminalKey(String key) {
        return key + terminalCharacter;
    }

    public static DatabaseReference getFollowRequestRef() {
        return FirebaseDatabase.getInstance().getReference("followrequests");
    }
}
