package cmput301f17t13.com.catisadog.models.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collection;
import java.util.Map;

/**
 * A singleton representing the currently authenticated user
 */
public class CurrentUser extends User {

    private FirebaseAuth mAuth;
    private static CurrentUser instance = null;

    private CurrentUser(String id, String username, Map<String, Boolean> followers, Map<String, Boolean> following) {
        super(id);
        setUsername(username);
        setFollowers(followers);
        setFollowing(following);

        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Get the current user instance. Returns null if no user is authenticated.
     * @return the current user
     */
    public static CurrentUser getInstance() {
        if(isAuthenticated()) return instance;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            signIn(user);
            return instance;
        }

        return null;
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
        if (!isAuthenticated()) {
            instance.mAuth.signOut();
            instance = null;
        }
    }

    public static boolean isAuthenticated() {
        return instance != null;
    }
}
