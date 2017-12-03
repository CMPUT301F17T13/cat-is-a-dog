// Remove JavaDoc issue (dangling comment)
package cmput301f17t13.com.catisadog;

import org.joda.time.DateTime;
import org.junit.Test;

import cmput301f17t13.com.catisadog.models.followrequest.FollowRequest;

import static org.junit.Assert.*;

public class FollowRequestTest {
    @Test
    public void createFollowRequestTest() throws Exception {
        DateTime request_timestamp = new DateTime();
        FollowRequest followRequest = new FollowRequest("username1", "username2", request_timestamp);

        assertEquals(followRequest.getFollower(), "username1");
        assertEquals(followRequest.getFollowee(), "username2");

        // Make sure the request timestamp is within 100ms of actual creation.
        assertTrue(followRequest.getRequestTimestamp().getMillis() <= request_timestamp.getMillis());
        assertTrue(followRequest.getRequestTimestamp().getMillis() > request_timestamp.getMillis()-100);
    }
}
