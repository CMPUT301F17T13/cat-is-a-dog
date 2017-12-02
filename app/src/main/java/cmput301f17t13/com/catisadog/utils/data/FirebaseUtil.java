/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.utils.data;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FirebaseUtil {

    private final static String datePattern = "yyyy-MM-dd";
    private final static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(datePattern);

    public static String dateToString(DateTime date) {
        return dateFormatter.print(date.toInstant());
    }

    public static DateTime stringToDate(String dateString) {
        return dateFormatter.parseDateTime(dateString);
    }

}
