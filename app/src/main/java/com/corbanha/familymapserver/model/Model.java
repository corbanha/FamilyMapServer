package com.corbanha.familymapserver.model;

import com.corbanha.familymapserver.ui.MapsFragment;
import com.corbanha.familymapserver.ui.SearchActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Model {


    private HashMap<String, Person> persons;
    private HashMap<String, Event> events;
    private Person user;

    private HashMap<String, Float> eventTypesToColors;
    private HashMap<String, ArrayList<String>> personIdToEventsId; //the personID with a list to all of their related events
    private HashMap<String, ArrayList<String>> personIdToChildrenId; //the person's children

    private HashMap<String, String> eventIdToSearchString;
    private HashMap<String, String> personIdToSearchString;

    private ArrayList<String> mothersSide;
    private ArrayList<String> fathersSide;

    private Settings settings;
    private Filter filter;
    private SignInDetails signInDetails;

    private static Model self;

    private Model(){
        persons = new HashMap<>();
        events = new HashMap<>();
        user = null;

        eventTypesToColors = new HashMap<>();
        personIdToEventsId = new HashMap<>();
        personIdToChildrenId = new HashMap<>();

        eventIdToSearchString = new HashMap<>();
        personIdToSearchString = new HashMap<>();

        mothersSide = new ArrayList<>();
        fathersSide = new ArrayList<>();

        settings = new Settings(); //creates it with the default settings automatically
        filter = new Filter(new ArrayList<String>());
    }

    public static Model getInstance(){
        if(self == null){
            self = new Model();
        }
        return self;
    }

    public static Model resetInstance(){
        self = null;
        return getInstance();
    }

    public void setBulkData(SignInDetails signInDetails, Person[] persons, Event[] events){

        this.signInDetails = signInDetails;

        for(int i = 0; persons != null && i < persons.length; i++){

            this.persons.put(persons[i].getPersonID(), persons[i]);
            personIdToSearchString.put(persons[i].getPersonID(), persons[i].searchString());

            //add the functionality for knowing the person's children
            if(persons[i].getFatherID() != null){
                if(!personIdToChildrenId.containsKey(persons[i].getFatherID()))
                    personIdToChildrenId.put(persons[i].getFatherID(), new ArrayList<String>());

                personIdToChildrenId.get(persons[i].getFatherID()).add(persons[i].getPersonID());
            }

            if(persons[i].getMotherID() != null){
                if(!personIdToChildrenId.containsKey(persons[i].getMotherID()))
                    personIdToChildrenId.put(persons[i].getMotherID(), new ArrayList<String>());

                personIdToChildrenId.get(persons[i].getMotherID()).add(persons[i].getPersonID());
            }
        }

        ArrayList<String> eventTypes = new ArrayList<>();

        for(int i = 0; events != null && i < events.length; i++){
            this.events.put(events[i].getEventID(), events[i]);
            eventIdToSearchString.put(events[i].getEventID(), events[i].searchString());

            //add the event type to the eventTypes array
            if(!eventTypes.contains(events[i].getEventType().toLowerCase().trim()))
                eventTypes.add(events[i].getEventType().toLowerCase().trim());

            //add the event id to the person -> events hashmap
            if(!personIdToEventsId.containsKey(events[i].getPersonID()))
                personIdToEventsId.put(events[i].getPersonID(), new ArrayList<String>());

            personIdToEventsId.get(events[i].getPersonID()).add(events[i].getEventID());
        }

        //sort all of the events for each person
        for(Map.Entry<String, ArrayList<String>> entry : personIdToEventsId.entrySet()){
            ArrayList<String> personsEvents = entry.getValue();

            Collections.sort(personsEvents, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    Event e1 = getEvent(o1);
                    Event e2 = getEvent(o2);
                    int yearCompare = e1.getYear() - e2.getYear();
                    if (yearCompare != 0) {
                        return yearCompare;
                    }else{
                        return e1.getEventType().compareTo(e2.getEventType());
                    }
                }
            });

            for(int i = 0; i < personsEvents.size(); i++){
                Event e = getEvent(personsEvents.get(i));
                if(e.getEventType().trim().equals("birth")){
                    String temp = personsEvents.get(i);
                    personsEvents.remove(i);
                    personsEvents.add(0, temp);
                }
                if(e.getEventType().trim().equals("death")){
                    String temp = personsEvents.get(i);
                    personsEvents.remove(i);
                    personsEvents.add(temp);
                }
            }
        }

        //set the marker colors for each given eventType
        Collections.sort(eventTypes);
        for(int i = 0; i < eventTypes.size(); i++){
            eventTypesToColors.put(eventTypes.get(i), MapsFragment.MAP_MARKER_COLORS[Math.min(i, MapsFragment.MAP_MARKER_COLORS.length - 1)]);
        }

        filter = new Filter(eventTypes); //set up the filter with the given eventTypes
    }

    public HashMap<String, Person> getPersons() {
        return persons;
    }

    public HashMap<String, Event> getEvents() {
        return events;
    }

    public ArrayList<Event> getFilterEvents(){
        ArrayList<Event> filteredEvents = new ArrayList<>();

        for(Map.Entry<String, Event> entry : events.entrySet()){
            Event e = entry.getValue();
            if(addEvent(e))
                filteredEvents.add(e);
        }

        return filteredEvents;
    }

    private boolean addEvent(Event e){
        if(e == null)
            return false;

        String eventType = e.getEventType();

        boolean addEvent = filter.isShowEventType(eventType);

        Person p = getPerson(e.getPersonID());

        if(p.getGender().equals("m")){
            addEvent = addEvent && filter.isShowMaleEvents();
        }else{
            addEvent = addEvent && filter.isShowFemaleEvents();
        }

        if(!filter.isShowFathersSide() && fathersSide.contains(p.getPersonID()))
            addEvent = false;

        if(!filter.isShowMothersSide() && mothersSide.contains(p.getPersonID()))
            addEvent = false;

        return addEvent;
    }

    public ArrayList<String> getPersonEvents(String personID){
        return personIdToEventsId.get(personID);
    }

    public ArrayList<String> getFilteredPersonEvents(String personID){
        ArrayList<String> unfilteredEvents = personIdToEventsId.get(personID);

        ArrayList<String> filteredEvents = new ArrayList<>();

        if(unfilteredEvents != null){
            for(int i = 0; i < unfilteredEvents.size(); i++){
                Event e = getEvent(unfilteredEvents.get(i));

                if(addEvent(e))
                    filteredEvents.add(unfilteredEvents.get(i));
            }
        }

        return filteredEvents;
    }

    public Person getPerson(String personID){
        return persons.get(personID);
    }

    public Event getEvent(String eventID){
        return events.get(eventID);
    }

    public float getMarkerColor(String eventType){
        return eventTypesToColors.get(eventType.toLowerCase().trim());
    }

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;

        mothersSide.clear();
        fathersSide.clear();

        //set the father's and mother's sides
        setFathersSide(user.getFatherID());
        setMothersSide(user.getMotherID());
    }

    private void setFathersSide(String personId){
        if(personId == null)
            return;

        Person p = getPerson(personId);
        if(p == null)
            return;

        fathersSide.add(personId);

        setFathersSide(p.getFatherID());
        setFathersSide(p.getMotherID());
    }

    private void setMothersSide(String personId){
        if(personId == null)
            return;

        Person p = getPerson(personId);
        if(p == null)
            return;

        mothersSide.add(personId);

        setMothersSide(p.getMotherID());
        setMothersSide(p.getFatherID());
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public boolean userLoggedIn(){
        return user != null;
    }

    public void logUserOut(){
        self = new Model();
    }

    public ArrayList<String> getChildren(String personID) {
        ArrayList<String> childList = personIdToChildrenId.get(personID);
        return childList == null ? new ArrayList<String>() : childList;
    }

    public SignInDetails getSignInDetails() {
        return signInDetails;
    }

    public void setSignInDetails(SignInDetails signInDetails) {
        this.signInDetails = signInDetails;
    }

    public ArrayList<SearchActivity.SearchResult> getFilteredSearchResults(String query){
        query = query.trim().toLowerCase();
        ArrayList<SearchActivity.SearchResult> results = new ArrayList<>();

        //add all of the persons who qualify
        for(Map.Entry<String, String> entry : personIdToSearchString.entrySet()){
            String searchString = entry.getValue();

            if(searchString.toLowerCase().contains(query)){
                results.add(new SearchActivity.SearchResult(true, entry.getKey()));
            }
        }

        for(Map.Entry<String, String> entry : eventIdToSearchString.entrySet()){
            String searchString = entry.getValue();

            Event e = getEvent(entry.getKey());

            if(searchString.toLowerCase().contains(query) && addEvent(e)){
                results.add(new SearchActivity.SearchResult(false, entry.getKey()));
            }
        }

        return results;
    }

    public static class SignInDetails{
        public String serverHost;
        public int serverPort;
        public String userName;
        public String password;

        public SignInDetails(String serverHost, int serverPort, String userName, String password){
            this.serverHost = serverHost;
            this.serverPort = serverPort;
            this.userName = userName;
            this.password = password;
        }
    }
}
