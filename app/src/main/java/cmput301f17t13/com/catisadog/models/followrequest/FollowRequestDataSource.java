package cmput301f17t13.com.catisadog.models.followrequest;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmput301f17t13.com.catisadog.utils.data.DataSource;

/**
 * Firebase Data Source for Follow Requests
 */
public class FollowRequestDataSource extends DataSource<FollowRequest>
    implements ValueEventListener {

    private static final String TAG = "FollowRequestDataSource";

    private ArrayList<FollowRequest> mFollowRequestArray;
    private boolean onlyShowUnaccepted;
    private boolean onlyShowAccepted;

    /**
     * Construct the Follow Request Data Source. To filter by follower or followee, set one of
     * the arguments to a String (and the other to null). For example, to find all the follow
     * requests where USERID is the follower, use FollowRequestDataSource(USERID, null).
     * @param follower
     * @param followee
     */
    public FollowRequestDataSource(String follower, String followee) {
        Query followRequestQuery = FirebaseDatabase.getInstance().getReference("followrequests/");

        if (follower != null && !follower.isEmpty()) {
            followRequestQuery = followRequestQuery.orderByChild("follower").equalTo(follower);
        }
        if (followee != null && !followee.isEmpty()) {
            followRequestQuery = followRequestQuery.orderByChild("followee").equalTo(followee);
        }

        followRequestQuery.addValueEventListener(this);

        mFollowRequestArray = new ArrayList<>();
    }

    /**
     * Further filter the follow request data source so accepted must be false.
     * @param onlyShowUnaccepted
     */
    public void setOnlyShowUnaccepted(boolean onlyShowUnaccepted) {
        this.onlyShowUnaccepted = onlyShowUnaccepted;
    }

    /**
     * Further filter the follow request data source so accepted must be true.
     * @param onlyShowAccepted
     */
    public void setOnlyShowAccepted(boolean onlyShowAccepted) {
        this.onlyShowAccepted = onlyShowAccepted;
    }

    /**
     * Get the data source reference (for adapters)
     * @return the data source reference
     */
    @Override
    public ArrayList<FollowRequest> getSource() { return mFollowRequestArray; }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mFollowRequestArray.clear();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            FollowRequest request = snapshot.getValue(FollowRequest.class);
            if (onlyShowUnaccepted && request.getAccepted()) continue;
            if (onlyShowAccepted && !request.getAccepted()) continue;
            mFollowRequestArray.add(request);
        }

        datasetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e(TAG, databaseError.getDetails());
    }

    private void datasetChanged() {
        setChanged();
        notifyObservers();
    }

}
