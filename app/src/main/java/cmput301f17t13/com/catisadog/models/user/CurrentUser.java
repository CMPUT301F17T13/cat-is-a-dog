package cmput301f17t13.com.catisadog.models.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A singleton representing the currently authenticated user
 */
public class CurrentUser extends User {

    private FirebaseAuth mAuth;
    private static CurrentUser instance = null;

    private CurrentUser(String id, String displayName, String email, String photoUrl) {
        super(id);
        setDisplayName(displayName);
        setEmail(email);
        setPhotoUrl(photoUrl);

        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Get the current user instance. Returns null if no user is authenticated.
     * @return the current user
     */
    public static CurrentUser getInstance() {
        if (isAuthenticated()) {
            return instance;
        }

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
        instance = new CurrentUser(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString());
    }

    /**
     * Sign out the currently authenticated user
     */
    public static void signOut() {
        if (isAuthenticated()) {
            instance.mAuth.signOut();
            instance = null;
        }
    }

    public static boolean isAuthenticated() {
        return instance != null;
    }
}
