package cmput301f17t13.com.catisadog.models.user;

import java.util.Collection;
import java.util.Map;

public class CurrentUser extends User {

    private static CurrentUser instance = null;

    private CurrentUser(String username, Map<String, Boolean> followers, Map<String, Boolean> following) {
        super(username);
        setFollowers(followers);
        setFollowing(following);
    }

    public static CurrentUser getInstance() {
        return instance;
    }

    public static void signIn(String username, Map<String, Boolean> followers, Map<String, Boolean> following) {
        instance = new CurrentUser(username, followers, following);
    }

    public static void signOut() {
        instance = null;
    }
}
