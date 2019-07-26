package services.results;

public class FillResult extends Result{

    /**
     * Creates a fill result with a given message
     * @param message either good/bad of how the fill result worked
     */
    public FillResult(String message){
        super(message);
    }
}