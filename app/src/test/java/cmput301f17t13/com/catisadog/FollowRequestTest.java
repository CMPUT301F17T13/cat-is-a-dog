// Remove JavaDoc issue (dangling comment)
package cmput301f17t13.com.catisadog;

import org.joda.time.DateTime;
import org.junit.Test;

import cmput301f17t13.com.catisadog.models.followrequest.FollowRequest;

import static org.junit.Assert.*;

public class FollowRequestTest {
    @Test
    public void createFollowRequestTest() throws Exception {
        long request_timestamp = DateTime.now().getMillis();
        FollowRequest followRequest = new FollowRequest("username1", "username2", request_timestamp);

        assertEquals(followRequest.getFollower(), "username1");
        assertEquals(followRequest.getFollowee(), "username2");

        // Make sure the request timestamp is within 100ms of actual creation.
        assertTrue(followRequest.getRequestTimestamp() <= request_timestamp);
        assertTrue(followRequest.getRequestTimestamp() > request_timestamp-100);
    }
}
