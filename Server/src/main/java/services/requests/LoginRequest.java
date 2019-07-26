package services.requests;

public class LoginRequest extends Request{

    private String userName;
    private String password;

    /**
     * A login request that is sent to the server to log the given user in
     * @param userName
     * @param password
     */
    public LoginRequest(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}