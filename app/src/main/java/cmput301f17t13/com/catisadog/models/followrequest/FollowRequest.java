package cmput301f17t13.com.catisadog.models.followrequest;

import org.joda.time.DateTime;

/**
 * A FollowRequest. Represents a relationship between two users, where one
 * user has made a follow request to another user. If the followee has accepted
 * the follower request, accepted is true.
 */
public class FollowRequest {
    private String key;
    private String follower;
    private String followee;
    private Boolean accepted;
    private long request_timestamp;

    public FollowRequest() {} // For Firebase use only

    public FollowRequest(String follower, String followee, DateTime createTime) {
        this.follower = follower;
        this.followee = followee;
        this.accepted = false;
        this.request_timestamp = createTime.getMillis();
    }

    public String getFollower() {
        return follower;
    }

    public String getFollowee() {
        return followee;
    }

    public DateTime getRequestTimestamp() {
        return new DateTime(this.request_timestamp);
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
