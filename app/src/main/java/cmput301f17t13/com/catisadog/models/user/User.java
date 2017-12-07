package cmput301f17t13.com.catisadog.models.user;

/**
 * Represents a User
 */
public class User {

    private String userId; // The user identifier
    private String email;  // The user email
    private String displayName; // The user display name
    private String photoUrl;    // The photo url

    public User() {} // only for Firebase

    public User(String userId) {
        this.userId = userId;
    }

    /** Getters and setters */

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
