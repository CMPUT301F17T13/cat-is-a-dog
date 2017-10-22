package cmput301f17t13.com.catisadog.models.user;

import java.util.Collection;

public class CurrentUser extends User {

    private static CurrentUser instance = null;

    private CurrentUser(String username, Collection<User> followers, Collection<User> following) {
        super(username);
        setFollowers(followers);
        setFollowing(following);
    }

    public static CurrentUser getInstance() {
        return instance;
    }

    public static void setUser(String username, Collection<User> followers, Collection<User> following) {
        instance = new CurrentUser(username, followers, following);
    }

}
