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
 * A Fragment for displaying a list of users that I am following.
 */
public class FollowingFragment extends Fragment
    implements Observer {

    private ArrayList<User> users;
    private ArrayList<FollowRequest> followRequests; // users I follow (accepted)
    private UserDataSource userDataSource;
    private FollowRequestDataSource followRequestDataSource;
    private String userId;

    private ListView followRequestsListView;
    private FollowRequestsAdapter followRequestsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_following, container, false);

        userId = CurrentUser.getInstance().getUserId();

        // Get follow requests where I am the follower, and the request has been accepted
        followRequestDataSource = new FollowRequestDataSource(userId, null);
        followRequestDataSource.setOnlyShowAccepted(true);
        followRequestDataSource.addObserver(this);
        followRequests = followRequestDataSource.getSource();

        // Get all users
        userDataSource = new UserDataSource(userId);
        userDataSource.addObserver(this);
        users = userDataSource.getSource();

        followRequestsListView = (ListView) view.findViewById(R.id.followingListView);
        followRequestsAdapter = new FollowRequestsAdapter(getActivity(), followRequests, users);
        followRequestsListView.setAdapter(followRequestsAdapter);

        return view;
    }


    @Override
    public void update(Observable observable, Object o) {
        followRequestsAdapter.notifyDataSetChanged();
    }

    /**
     * An adapter for converting follow requests into a list view.
     */
    private class FollowRequestsAdapter extends ArrayAdapter<FollowRequest> {

        private FollowRequestRepository followRequestRepository;
        private ArrayList<User> users;

        public FollowRequestsAdapter(Context context, ArrayList<FollowRequest> followRequests, ArrayList<User> users) {
            super(context, 0, followRequests);
            this.followRequestRepository = new FollowRequestRepository();
            this.users = users;
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

            final User user = getUserForUserId(followRequest.getFollowee());

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
