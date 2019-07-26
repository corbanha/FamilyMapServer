package services.requests;

public class PersonsRequest extends Request {

    private String authToken;

    /**
     * Returns all of the members for the current user as determined by the given AuthToken
     * @param authToken
     */
    public PersonsRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}