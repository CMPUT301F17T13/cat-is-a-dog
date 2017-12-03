package cmput301f17t13.com.catisadog.models.followrequest;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cmput301f17t13.com.catisadog.utils.data.OnResultListener;
import cmput301f17t13.com.catisadog.utils.data.Repository;

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

    @Override
    public void get(String key, final OnResultListener<FollowRequest> resultListener) {
        mFollowRequestRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FollowRequest request = dataSnapshot.getValue(FollowRequest.class);
                resultListener.onResult(request);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
