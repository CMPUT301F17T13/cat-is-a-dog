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
 * Fragment for searching through users
 */
public class SearchUsersFragment extends Fragment
    implements Observer {

    private ArrayList<User> users;
    private ArrayList<FollowRequest> following; // users I have sent follow requests to
    private UserDataSource userDataSource;
    private FollowRequestDataSource followRequestDataSource;
    private String userId;

    private ListView usersListView;
    private UsersAdapter usersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_users, container, false);

        userId = CurrentUser.getInstance().getUserId();

        userDataSource = new UserDataSource(userId);
        userDataSource.addObserver(this);
        users = userDataSource.getSource();

        followRequestDataSource = new FollowRequestDataSource(userId, null);
        followRequestDataSource.addObserver(this);
        following = followRequestDataSource.getSource();

        usersListView = (ListView) view.findViewById(R.id.usersListView);
        usersAdapter = new UsersAdapter(getActivity(), users, following);
        usersListView.setAdapter(usersAdapter);

        return view;
    }


    @Override
    public void update(Observable observable, Object o) {
        usersAdapter.notifyDataSetChanged();
    }

    /**
     * An adapter for converting user objects into users to be displayed in a list
     * view.
     */
    private class UsersAdapter extends ArrayAdapter<User> {

        FollowRequestRepository followRequestRepository;
        ArrayList<FollowRequest> following; // users I have sent follow requests to

        public UsersAdapter(Context context, ArrayList<User> users, ArrayList<FollowRequest> following) {
            super(context, 0, users);
            this.followRequestRepository = new FollowRequestRepository();
            this.following = following;
        }

        private String getActionButtonTextForUser(String userId) {
            for (FollowRequest followRequest : following) {
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

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final User user = this.getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
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
