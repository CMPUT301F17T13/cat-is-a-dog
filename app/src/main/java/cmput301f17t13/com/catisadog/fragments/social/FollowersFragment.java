package cmput301f17t13.com.catisadog.fragments.social;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.models.followrequest.FollowRequest;
import cmput301f17t13.com.catisadog.models.followrequest.FollowRequestDataSource;
import cmput301f17t13.com.catisadog.models.followrequest.FollowRequestRepository;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.models.user.User;
import cmput301f17t13.com.catisadog.models.user.UserDataSource;
import cmput301f17t13.com.catisadog.utils.DownloadImageTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersFragment extends Fragment
    implements Observer {

    private ArrayList<User> users;
    private ArrayList<FollowRequest> followers; // users that follow me
    private ArrayList<FollowRequest> followRequests; // users that I have sent follow requests to
    private UserDataSource userDataSource;
    private FollowRequestDataSource followersDataSource;
    private FollowRequestDataSource followRequestDataSource;
    private String userId;

    private ListView followRequestsListView;
    private FollowersAdapter followersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);

        userId = CurrentUser.getInstance().getUserId();

        followersDataSource = new FollowRequestDataSource(null, userId);
        followersDataSource.setOnlyShowAccepted(true);
        followersDataSource.addObserver(this);
        followers = followersDataSource.getSource();

        followRequestDataSource = new FollowRequestDataSource(userId, null);
        followRequestDataSource.addObserver(this);
        followRequests = followRequestDataSource.getSource();

        userDataSource = new UserDataSource(userId);
        userDataSource.addObserver(this);
        users = userDataSource.getSource();

        followRequestsListView = (ListView) view.findViewById(R.id.followersListView);
        followersAdapter = new FollowersAdapter(getActivity(), followers, users, followRequests);
        followRequestsListView.setAdapter(followersAdapter);

        return view;
    }


    @Override
    public void update(Observable observable, Object o) {
        followersAdapter.notifyDataSetChanged();
    }

    /**
     * An adapter for converting follow requests into a list view.
     */
    private class FollowersAdapter extends ArrayAdapter<FollowRequest> {

        private FollowRequestRepository followRequestRepository;
        private ArrayList<User> users;
        private ArrayList<FollowRequest> followRequests; // users I have sent follow requests to

        public FollowersAdapter(Context context, ArrayList<FollowRequest> followers,
                                ArrayList<User> users, ArrayList<FollowRequest> followRequests)
        {
            super(context, 0, followers);
            this.followRequestRepository = new FollowRequestRepository();
            this.users = users;
            this.followRequests = followRequests;
        }

        private String getActionButtonTextForUser(String userId) {
            for (FollowRequest followRequest : followRequests) {
                if (followRequest.getFollowee().equals(userId)) {
                    if (followRequest.getAccepted()) {
                        return getString(R.string.social_action_following);
                    } else {
                        return getString(R.string.social_action_req_sent);
                    }
                }
            }
            return getString(R.string.social_action_follow);
        }

        private User getUserForUserId(String userId) {
            for (User user : users) {
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final FollowRequest followRequest = this.getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            }

            final User user = getUserForUserId(followRequest.getFollower());

            if (user == null) {
                return convertView;
            }

            TextView displayNameView = (TextView) convertView.findViewById(R.id.userDisplayName);
            TextView emailView = (TextView) convertView.findViewById(R.id.userEmail);
            ImageView photoView = (ImageView) convertView.findViewById(R.id.userPhoto);
            final Button socialActionButton = (Button) convertView.findViewById(R.id.socialActionButton);
            socialActionButton.setText(getActionButtonTextForUser(user.getUserId()));
            if (!socialActionButton.getText().equals(getString(R.string.social_action_follow))) {
                socialActionButton.setEnabled(false);
            }

            displayNameView.setText(user.getDisplayName());
            emailView.setText(user.getEmail());
            photoView.setTag(user.getPhotoUrl());
            new DownloadImageTask().execute(photoView);

            socialActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (socialActionButton.getText().equals(getString(R.string.social_action_follow))) {
                        FollowRequest followRequest = new FollowRequest(userId, user.getUserId(), new DateTime());
                        followRequestRepository.add(followRequest);
                    }
                }
            });

            return convertView;
        }
    }
}