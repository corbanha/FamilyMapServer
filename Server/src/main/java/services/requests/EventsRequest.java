package services.requests;

public class EventsRequest extends Request {

    private String authToken;

    /**
     * Returns all of the events for all of the family members of the current user as determined by
     * the given authToken.
     * @param authToken
     */
    public EventsRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}