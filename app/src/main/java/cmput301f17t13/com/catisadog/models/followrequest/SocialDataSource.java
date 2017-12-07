/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.models.followrequest;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.models.user.User;
import cmput301f17t13.com.catisadog.models.user.UserRepository;
import cmput301f17t13.com.catisadog.utils.data.DataSource;
import cmput301f17t13.com.catisadog.utils.data.FirebaseUtil;
import cmput301f17t13.com.catisadog.utils.data.Repository;


/**
 * This is a data source for the list of {@link User} that the {@link CurrentUser} is following
 * or the list of {@link User} that are followers of the {@link CurrentUser}. This is achieved by
 * listening on the {@link FollowRequest} items related to the current user and then fetching the
 * individual {@link User} objects.
 */
public class SocialDataSource extends DataSource<User>
    implements ValueEventListener, Repository.OnResultListener<User> {

    public enum UserType { FOLLOWING, FOLLOWER }

    private String userId;
    private UserType userType;
    private ArrayList<User> otherUsers;
    private Repository<User> userRepository;
    private Query otherUserQuery;

    public SocialDataSource(String userId, UserType type) {
        this.userId = userId;
        this.userType = type;
        otherUsers = new ArrayList<>();
        userRepository = new UserRepository();
    }

    /**
     * First we listen on the follow requests related to the current user. This is the first step
     * in our query chain.
     */
    @Override
    public void open() {
        otherUsers.clear();
        if (userType == UserType.FOLLOWING) {
            otherUserQuery = FirebaseUtil.getFollowRequestRef()
                    .orderByChild("follower").equalTo(userId);
        }
        else {
            otherUserQuery = FirebaseUtil.getFollowRequestRef()
                    .orderByChild("followee").equalTo(userId);
        }

        otherUserQuery.addValueEventListener(this);
    }

    @Override
    public ArrayList<User> getSource() {
        return otherUsers;
    }

    /**
     * Once the {@link FollowRequest} is received we then query Firebase for the the actual
     * {@link User} object. This is the second step in our query chain.
     * @param dataSnapshot
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            FollowRequest request = snapshot.getValue(FollowRequest.class);

            if (request != null && request.getAccepted()) {
                if (userType == UserType.FOLLOWING) {
                    userRepository.get(request.getFollowee(), this);
                } else {
                    userRepository.get(request.getFollower(), this);
                }
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        close();
    }

    /**
     * Finally we receive the relevant {@link User} which we requested and notify observers.
     * This is the last step in our query chain.
     *
     * @param user the {@link User} we requested
     */
    @Override
    public void onResult(User user) {
        otherUsers.add(user);

        Collections.sort(otherUsers, new Comparator<User>() {
            @Override
            public int compare(User one, User two) {
                return one.getDisplayName().compareTo(two.getDisplayName());
            }
        });

        setChanged();
        notifyObservers(user);
    }

    @Override
    public void close() {
        otherUserQuery.removeEventListener(this);
    }
}
