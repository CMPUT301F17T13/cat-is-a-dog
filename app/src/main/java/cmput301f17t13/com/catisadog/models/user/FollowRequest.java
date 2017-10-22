package cmput301f17t13.com.catisadog.models.user;

import org.joda.time.DateTime;

public class FollowRequest {
    private String requester;
    private String requestee;
    private long request_timestamp;

    public FollowRequest(String requester, String requestee, DateTime createTime) {
        this.requester = requester;
        this.requestee = requestee;
        this.request_timestamp = createTime.getMillis();
    }

    public String getRequester() {
        return requester;
    }

    public String getRequestee() {
        return requestee;
    }

    public DateTime getRequestTimestamp() {
        return new DateTime(this.request_timestamp);
    }
}
