/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.user;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cmput301f17t13.com.catisadog.utils.data.OnResultListener;
import cmput301f17t13.com.catisadog.utils.data.Repository;


public class FollowRepository implements Repository<User> {

    private DatabaseReference followRef;

    public FollowRepository() {
        followRef = FirebaseDatabase.getInstance().getReference("follows");
    }

    @Override
    public void add(User user) {
        String userId = user.getUserId();
        followRef.child(userId).setValue(user);
    }

    @Override
    public void update(String key, User user) {
        followRef.child(key).setValue(user);
    }

    @Override
    public void delete(String key) {
        followRef.child(key).removeValue();
    }

    @Override
    public void get(String key, final OnResultListener<User> resultListener) {
        followRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                resultListener.onResult(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
