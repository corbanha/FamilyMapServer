package services.requests;

import model.Event;
import model.Person;
import model.User;

public class LoadRequest extends Request {
    private User[] users;
    private Person[] persons;
    private Event[] events;

    /**
     * The "users" property in the request body contains an array of users to be
     * created. The "persons" and "events" properties contain family history information for these
     * users. The objects contained in the "persons" and "events" arrays should be added to the
     * server’s database. The objects in the "users" array have the same format as those passed to
     * the /user/register API with the addition of the personID. The objects in the "persons" array have
     * the same format as those returned by the /person/[personID] API. The objects in the "events"
     * array have the same format as those returned by the /event/[eventID] API.
     * @param users an array of users to be created
     * @param persons ancestors of those in the users array
     * @param events family history events of those in the users array
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events){
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    @Override
    public String toString() {
        String str = "";

        str += "Users:\n";
        for(int i = 0, len = users.length; i < len; i++){
            str += users[i].toString() + "\n";
        }

        str += "Persons:\n";
        for(int i = 0, len = persons.length; i < len; i++){
            str += persons[i].toString() + "\n";
        }

        str += "Events:\n";
        for(int i = 0, len = events.length; i < len; i++){
            str += events[i].toString() + "\n";
        }

        return str;
    }
}
