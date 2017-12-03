package cmput301f17t13.com.catisadog.models.user;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmput301f17t13.com.catisadog.utils.data.DataSource;


/**
 * Firebase Data Source for other Users
 */
public class UserDataSource extends DataSource<User>
    implements ValueEventListener {

    private static final String TAG = "UserDataSource";

    private String currentUserId;
    private ArrayList<User> mOtherUsersArray;

    public UserDataSource(String currentUserId) {
        this.currentUserId = currentUserId;

        Query otherUsersQuery = FirebaseDatabase.getInstance().getReference("users/");
        otherUsersQuery.addValueEventListener(this);

        mOtherUsersArray = new ArrayList<>();
    }

    /**
     * Get the data source reference (for adapters)
     * @return the data source reference
     */
    @Override
    public ArrayList<User> getSource() { return mOtherUsersArray; }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mOtherUsersArray.clear();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            User user = snapshot.getValue(User.class);
//            if (user.getUserId().equals(currentUserId)) continue;
            mOtherUsersArray.add(user);
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
