package com.corbanha.familymapserver.model.results;

import com.corbanha.familymapserver.model.Event;

public class MultiEventResult extends Result {

    private Event[] events;

    /**
     * Creates a Multi Person Result object with an error message
     * @param message the error message that resulted when trying to get the person
     */
    public MultiEventResult(String message){
        super(message);
        this.events = new Event[0];
    }

    /**
     * Creates a Multi Person Result object with the associated events that were obtained
     * @param events the person found in the search
     */
    public MultiEventResult(Event[] events){
        super("");
        this.events = events;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
