package cmput301f17t13.com.catisadog.models.followrequest;

import org.joda.time.DateTime;

/**
 * A FollowRequest. Represents a relationship between two users, where one
 * user has made a follow request to another user. If the followee has accepted
 * the follower request, accepted is true.
 */
public class FollowRequest {
    private String key;         // The follow request firebase key
    private String follower;    // The firebase key of the user making the request
    private String followee;    // The firebase key for the user being asked to be followed
    private Boolean accepted;   // Whether the followee has accepted the follow request
    private long requestTimestamp;

    public FollowRequest() {} // For Firebase use only

    /**
     * Instantiate the follow request object
     * @param follower The firebase key of the user making the request
     * @param followee The firebase key for the user being asked to be followed
     * @param createTime The creation time of the follow request
     */
    public FollowRequest(String follower, String followee, long createTime) {
        this.follower = follower;
        this.followee = followee;
        this.accepted = false;
        this.requestTimestamp = createTime;
    }

    public String getFollower() {
        return follower;
    }

    public String getFollowee() {
        return followee;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
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
