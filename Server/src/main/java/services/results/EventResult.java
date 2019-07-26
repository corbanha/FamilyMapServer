package services.results;

import model.Event;

public class EventResult extends Result {

    private Event event;

    /**
     * Creates an event Result object with an error message
     * @param message the error message that resulted when trying to get the event
     */
    public EventResult(String message){
        super(message);
        event = null;
    }

    /**
     * Creates a event Result object with the associated event that was obtained
     * @param event the event found in the search
     */
    public EventResult(Event event){
        super("");
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
