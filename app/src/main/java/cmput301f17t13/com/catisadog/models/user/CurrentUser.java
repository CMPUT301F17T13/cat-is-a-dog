package cmput301f17t13.com.catisadog.models.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collection;
import java.util.Map;

/**
 * A singleton representing the currently authenticated user
 */
public class CurrentUser extends User {

    private String userId;

    private FirebaseAuth mAuth;
    private static CurrentUser instance = null;

    private CurrentUser(String id, String username, Map<String, Boolean> followers, Map<String, Boolean> following) {
        super(username);
        setFollowers(followers);
        setFollowing(following);

        mAuth = FirebaseAuth.getInstance();
        userId = id;
    }

    /**
     * Get the current user instance. Returns null if no user is authenticated.
     * @return the current user
     */
    public static CurrentUser getInstance() {
        return instance;
    }

    /**
     * Sign in a valid firebase user.
     * @param user firebase user
     */
    public static void signIn(FirebaseUser user) {
        // TODO(#39): Get followers and following from Firebase
        instance = new CurrentUser(user.getUid(), user.getEmail(), null, null);
    }

    /**
     * Sign out the currently authenticated user
     */
    public static void signOut() {
        instance.mAuth.signOut();
        instance = null;
    }

    public String getUserId() {
        return userId;
    }
}
