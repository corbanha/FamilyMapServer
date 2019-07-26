package services.results;

public class ClearResult extends Result{

    /**
     * Creates a clear result with a given message
     * @param message the message either good/bad based on how the clear command worked
     */
    public ClearResult(String message){
        super(message);
    }

}