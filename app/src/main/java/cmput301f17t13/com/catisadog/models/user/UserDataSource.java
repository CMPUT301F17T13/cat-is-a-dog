package cmput301f17t13.com.catisadog.models.user;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    private Query otherUsersQuery;

    public UserDataSource(String currentUserId) {
        this.currentUserId = currentUserId;

        mOtherUsersArray = new ArrayList<>();
    }

    @Override
    public void open() {
        mOtherUsersArray.clear();
        otherUsersQuery = FirebaseDatabase.getInstance().getReference("users");
        otherUsersQuery.addValueEventListener(this);
    }

    /**
     * Get the data source reference (for adapters)
     * @return the data source reference
     */
    @Override
    public ArrayList<User> getSource() { return mOtherUsersArray; }

    /**
     * Get all users, except the current user
     * @param dataSnapshot the root
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mOtherUsersArray.clear();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
            User user = snapshot.getValue(User.class);
            if (user.getUserId().equals(currentUserId)) continue;
            mOtherUsersArray.add(user);
        }

        datasetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.e(TAG, databaseError.getDetails());
        close();
    }

    /**
     * Clean up user query listener
     */
    @Override
    public void close() {
        otherUsersQuery.removeEventListener(this);
    }
}
