package services.results;

public class LoginResult extends Result{
    private String authToken;
    private String userName;
    private String personID;

    /**
     * Sets up a Login Result with an error message.
     * The authToken, userName and personID are all set to empty strings
     * @param message the error message
     */
    public LoginResult(String message){
        super(message);
        authToken = "";
        userName = "";
        personID = "";
    }

    /**
     * Sets up a Login Result with a blank message, and other attributes set up
     * @param authToken the authentication token for the user's instance
     * @param userName the username of the user
     * @param personID the associated person ID for the user
     */
    public LoginResult(String authToken, String userName, String personID){
        super("");
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}