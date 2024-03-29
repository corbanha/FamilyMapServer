package services.requests;

public class PersonRequest extends Request {

    private String personID;
    private String authToken;

    /**
     * Creates a request for a person with the given personID and authToken
     * @param personID
     * @param authToken
     */
    public PersonRequest(String personID, String authToken) {
        this.personID = personID;
        this.authToken = authToken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}