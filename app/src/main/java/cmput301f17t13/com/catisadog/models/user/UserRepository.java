/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.user;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cmput301f17t13.com.catisadog.utils.data.Repository;

public class UserRepository implements Repository<User> {

    private DatabaseReference usersRef;

    public UserRepository() {
        usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public void add(User user) {
        String userId = user.getUserId();
        usersRef.child(userId).setValue(user);
    }

    @Override
    public void update(String key, User user) {
        usersRef.child(key).setValue(user);
    }

    @Override
    public void delete(String key) {
        usersRef.child(key).removeValue();
    }

}
