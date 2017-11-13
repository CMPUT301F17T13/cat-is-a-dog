// Remove JavaDoc issue (dangling comment)
package cmput301f17t13.com.catisadog;

import org.junit.Test;

import java.util.HashMap;
import cmput301f17t13.com.catisadog.models.user.CurrentUser;
import cmput301f17t13.com.catisadog.models.user.User;
import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void createUserTest() throws Exception {
        User user = new User("username");

        assertEquals(user.getUsername(), "username");
        assertTrue(user.getFollowers().size() == 0);
        assertTrue(user.getFollowing().size() == 0);

        // cannot add a null user
        try {
            User user2 = new User(null);
            assertTrue(false);
        } catch(Exception e) { // Empty catch block
            assertTrue(true);
        }
    }

    @Test
    public void createCurrentUserTest() throws Exception {
        HashMap<String, Boolean> followers = new HashMap<>();
        HashMap<String, Boolean> following = new HashMap<>();

        //CurrentUser.signIn("username", followers, following);
        //CurrentUser user = CurrentUser.getInstance();

        //assertEquals(user.getUsername(), "username");
        //assertTrue(user.getFollowers().size() == 0);
        //assertTrue(user.getFollowing().size() == 0);
    }

    @Test
    public void addFollowerFollowingUserTest() throws Exception {
        User user = new User("main_user");

        HashMap<String, Boolean> followers = new HashMap<>();
        followers.put("follower1", true);
        followers.put("follower2", true);
        user.setFollowers(followers);

        HashMap<String, Boolean> following = new HashMap<>();
        following.put("following1", true);
        following.put("following2", true);
        following.put("following3", true);
        user.setFollowing(following);

        assertTrue(user.getFollowers().size() == 2);
        assertTrue(user.getFollowing().size() == 3);

        assertTrue(user.getFollowers().containsKey("follower1"));
        assertTrue(user.getFollowing().containsKey("following1"));
        assertFalse(user.getFollowing().containsKey(null));

        HashMap<String, Boolean> nullFollowing = new HashMap<>();
        nullFollowing.put(null, true);

        // cannot add a null user
        try {
            user.setFollowing(nullFollowing);
            assertTrue(false);
        } catch(Exception e) { // Fix empty try/catch
            assertTrue(true);
        }
    }
}
