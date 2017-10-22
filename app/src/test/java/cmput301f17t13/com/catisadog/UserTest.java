/**
 * Created by nathan on 22/10/17.
 */
package cmput301f17t13.com.catisadog;

import org.junit.Test;

import cmput301f17t13.com.catisadog.models.user.User;

import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void createUserTest() throws Exception {
        User user = new User("username");
        assertEquals(user.getUsername(), "username");
        assertTrue(user.getFollowers().size() == 0);
        assertTrue(user.getFollowing().size() == 0);


    }
}
