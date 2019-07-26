package services.requests;

public class FillRequest extends Request {

    private String userName;
    private int generations;

    /**
     * Completes a fill request for a given user with the specified number of generations
     * @param userName
     * @param generations
     */
    public FillRequest(String userName, int generations) {
        this.userName = userName;
        this.generations = generations;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}