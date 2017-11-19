/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.activities.history;

import android.app.Activity;
import android.location.Location;

import java.util.List;

import cmput301f17t13.com.catisadog.models.habitevent.HabitEvent;

public class HabitHistoryActivity extends Activity {
    private List<HabitEvent> currentUserHabitEvents;

    // It is IMPORTANT that these are defined as package-level functions
    List<HabitEvent> getCurrentUserHabitEvents() {
        return currentUserHabitEvents;
    }

    List<HabitEvent> getEventsByLocation(Location location) {
        return null;
    }

    List<HabitEvent> getEventsByHabitType(String type) {
        return null;
    }

}
