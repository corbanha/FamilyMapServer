package services.requests;

public class EventRequest extends Request {

    private String eventID;
    private String authToken;

    /**
     * Creates an EventRequest for an event with the given EventID and authToken
     * @param eventID
     * @param authToken
     */
    public EventRequest(String eventID, String authToken) {
        this.eventID = eventID;
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}