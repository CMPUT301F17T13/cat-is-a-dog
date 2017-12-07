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

import java.util.TimeZone;

/**
 * Static method holder for Firebase utility functions
 */
public class FirebaseUtil {

    private final static char terminalCharacter = '\uf8ff';
    private final static String datePattern = "yyyy-MM-dd";
    private final static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(datePattern);

    private static DateTimeZone localTimeZone = DateTimeZone.forTimeZone(TimeZone.getDefault());

    /**
     * Date format stored in firebase will be local time string
     * yyyy-MM-dd was picked because it is sortable
     * @param date UTC datetime
     * @return yyyy-MM-dd format date string of date converted to local time
     */
    public static String dateToString(DateTime date) {
        date = date.toDateTime(localTimeZone);
        return dateFormatter.print(date.toInstant());
    }

    /**
     * Convert from date format stored in firebase to datetime
     * @param dateString the date string to parse
     * @return corresponding datetime object
     */
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

    /**
     * Get Database Reference to follow request index in the database
     * @return the reference to the root index
     */
    public static DatabaseReference getFollowRequestRef() {
        return FirebaseDatabase.getInstance().getReference("followrequests");
    }
}
