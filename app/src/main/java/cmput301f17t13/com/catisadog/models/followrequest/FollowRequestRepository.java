package cmput301f17t13.com.catisadog.models.followrequest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cmput301f17t13.com.catisadog.utils.data.Repository;

/**
 * Created by kevin on 2017-12-02.
 */

public class FollowRequestRepository implements Repository<FollowRequest> {

    private DatabaseReference mFollowRequestRef;

    public FollowRequestRepository() {
        mFollowRequestRef = FirebaseDatabase.getInstance().getReference("followrequests/");
    }

    @Override
    public void add(FollowRequest followRequest) {
        DatabaseReference newRef = mFollowRequestRef.push();
        String key = newRef.getKey();
        followRequest.setKey(key);
        newRef.setValue(followRequest);
    }

    @Override
    public void update(String key, FollowRequest followRequest) {
        mFollowRequestRef.child(key).setValue(followRequest);
    }

    @Override
    public void delete(String key) {
        mFollowRequestRef.child(key).removeValue();
    }
}
