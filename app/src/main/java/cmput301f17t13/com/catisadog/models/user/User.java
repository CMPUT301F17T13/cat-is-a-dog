package cmput301f17t13.com.catisadog.models.user;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String username;
    private Map<String, Boolean> followers;
    private Map<String, Boolean> following;

    public User(String username) {
        this.followers = new HashMap<>();
        this.following = new HashMap<>();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void follow(User otherUser) {
        // Sends a follow request
    }

    public List<FollowRequest> getRequests() {
        return null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Boolean> getFollowers() {
        return followers;
    }

    public void setFollowers(Map<String, Boolean> followers) {
        this.followers = followers;
    }

    public Map<String, Boolean> getFollowing() {
        return following;
    }

    public void setFollowing(Map<String, Boolean> following) {
        this.following = following;
    }
}
