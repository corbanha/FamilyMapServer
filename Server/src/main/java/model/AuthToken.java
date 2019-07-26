package model;

public class AuthToken {

    private String userName;
    private String token;

    /**
     * Creates an AuthToken with all of the given attributes
     * @param userName
     * @param token
     */
    public AuthToken(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return userName + " " + token;
    }

    /**
     * Checks if an object is equal to this one
     * @param o the object to check against
     * @return true if the objects are the same, false if there is a difference
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof AuthToken) {
            AuthToken a = (AuthToken) o;
            return a.getUserName().equals(getUserName()) && a.getToken().equals(getToken());

        }
        return false;
    }
}