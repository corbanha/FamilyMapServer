package com.corbanha.familymapserver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Filter {

    private boolean showFemaleEvents = true;
    private boolean showMaleEvents = true;
    private boolean showFathersSide = true;
    private boolean showMothersSide = true;

    private HashMap<String, Boolean> eventTypeToShow;
    private ArrayList<String> eventTypes;

    public Filter(ArrayList<String> eventTypes){
        eventTypeToShow = new HashMap<>();
        this.eventTypes = eventTypes;

        for(String eType : eventTypes)
            eventTypeToShow.put(eType.trim().toLowerCase(), true);
    }

    public List<String> getFilterOptions(){
        List<String> filters = new ArrayList<>();

        filters.addAll(eventTypes);

        Collections.sort(filters);

        filters.add("Father's Side");
        filters.add("Mother's Side");
        filters.add("Male Events");
        filters.add("Female Events");

        return filters;
    }

    public boolean isShowEventType(String eventType){
        eventType = eventType.trim().toLowerCase();

        if(eventType.equals("father's side"))
            return isShowFathersSide();
        if(eventType.equals("mother's side"))
            return isShowMothersSide();
        if(eventType.equals("male events"))
            return isShowMaleEvents();
        if(eventType.equals("female events"))
            return isShowFemaleEvents();

        if(eventTypeToShow.containsKey(eventType))
            return eventTypeToShow.get(eventType);
        return false;
    }

    public void setShowEventType(String eventType, boolean set){
        eventType = eventType.trim().toLowerCase();

        if(eventType.equals("father's side"))
            setShowFathersSide(set);
        if(eventType.equals("mother's side"))
            setShowMothersSide(set);
        if(eventType.equals("male events"))
            setShowMaleEvents(set);
        if(eventType.equals("female events"))
            setShowFemaleEvents(set);

        if(eventTypeToShow.containsKey(eventType))
            eventTypeToShow.put(eventType, set);
    }

    public boolean isShowFemaleEvents() {
        return showFemaleEvents;
    }

    public void setShowFemaleEvents(boolean showFemaleEvents) {
        this.showFemaleEvents = showFemaleEvents;
    }

    public boolean isShowMaleEvents() {
        return showMaleEvents;
    }

    public void setShowMaleEvents(boolean showMaleEvents) {
        this.showMaleEvents = showMaleEvents;
    }

    public boolean isShowFathersSide() {
        return showFathersSide;
    }

    public void setShowFathersSide(boolean showFathersSide) {
        this.showFathersSide = showFathersSide;
    }

    public boolean isShowMothersSide() {
        return showMothersSide;
    }

    public void setShowMothersSide(boolean showMothersSide) {
        this.showMothersSide = showMothersSide;
    }
}
