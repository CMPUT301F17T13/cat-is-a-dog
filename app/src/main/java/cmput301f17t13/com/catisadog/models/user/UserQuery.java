/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.user;


import android.os.AsyncTask;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cmput301f17t13.com.catisadog.models.user.User;

public class UserQuery extends AsyncTask<String, Void, User>
    implements ValueEventListener {

    private DatabaseReference userRef;
    private CountDownLatch completeSignal;
    private User result = null;

    public UserQuery() {
        userRef = FirebaseDatabase.getInstance().getReference("users");
        completeSignal = new CountDownLatch(1);
    }

    @Override
    protected User doInBackground(String... strings) {

        String username = strings[0];

        userRef.orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(this);

        try {
            completeSignal.await(1000, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            return null;
        }

        return result;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            result = dataSnapshot.getValue(User.class);
        }

        completeSignal.countDown();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Shortcut completion
        completeSignal.countDown();
    }
}
