package cmput301f17t13.com.catisadog.models.user;

import java.util.Collection;
import java.util.List;

public class User {
    private String username;
    private Collection<String> followers;
    private Collection<String> following;

    public User(String username) {
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

    public Collection<String> getFollowers() {
        return followers;
    }

    public void setFollowers(Collection<String> followers) {
        this.followers = followers;
    }

    public Collection<String> getFollowing() {
        return following;
    }

    public void setFollowing(Collection<String> following) {
        this.following = following;
    }
}
