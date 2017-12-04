package cmput301f17t13.com.catisadog.models.followrequest;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cmput301f17t13.com.catisadog.utils.data.OnResultListener;
import cmput301f17t13.com.catisadog.utils.data.Repository;

/**
 * Follow Request Repository. Use this to create, read, update, and delete
 * a FollowRequest in the Firebase store.
 */
public class FollowRequestRepository implements Repository<FollowRequest> {

    private DatabaseReference mFollowRequestRef;

    public FollowRequestRepository() {
        mFollowRequestRef = FirebaseDatabase.getInstance().getReference("followrequests/");
    }

    /**
     * Add a new FollowRequest to Firebase.
     * @param followRequest
     */
    @Override
    public void add(FollowRequest followRequest) {
        DatabaseReference newRef = mFollowRequestRef.push();
        String key = newRef.getKey();
        followRequest.setKey(key);
        newRef.setValue(followRequest);
    }

    /**
     * Update a FollowRequest in Firebase.
     * @param key
     * @param followRequest
     */
    @Override
    public void update(String key, FollowRequest followRequest) {
        mFollowRequestRef.child(key).setValue(followRequest);
    }

    /**
     * Delete a FollowRequest in Firebase.
     * @param key
     */
    @Override
    public void delete(String key) {
        mFollowRequestRef.child(key).removeValue();
    }

    /**
     * Get a FollowRequest from Firebase.
     * @param key
     * @param resultListener
     */
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
