package cmput301f17t13.com.catisadog.models.user;

import org.joda.time.DateTime;

public class FollowRequest {
    private User requester;
    private User requestee;
    private DateTime requestDate;

    public FollowRequest(User requester, User requestee) {
        this.requester = requester;
        this.requestee = requestee;
        this.requestDate = new DateTime();
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getRequestee() {
        return requestee;
    }

    public void setRequestee(User requestee) {
        this.requestee = requestee;
    }

    public DateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(DateTime requestDate) {
        this.requestDate = requestDate;
    }
}
