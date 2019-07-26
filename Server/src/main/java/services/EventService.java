package services;

import java.util.ArrayList;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.EventDAO;
import handlers.Server;
import model.AuthToken;
import model.Event;
import services.requests.EventRequest;
import services.requests.EventsRequest;
import services.results.EventResult;
import services.results.MultiEventResult;
import services.results.PersonResult;

public class EventService extends Service {

    /**
     * Returns the single Event object with the specified ID.
     * @param er the EventRequest of which particular event to get
     * @return the result of the search for the given event ID
     */
    public EventResult getEvent(EventRequest er){
        boolean success = false;
        try{
            Server.getDatabase().openConnection();

            EventDAO edao = new EventDAO(Server.getDatabase().getConnection());
            AuthTokenDAO adao = new AuthTokenDAO(Server.getDatabase().getConnection());

            AuthToken a = adao.find(er.getAuthToken());

            if(a == null){
                return new EventResult("You are not logged in, please log in");
            }

            Event e = edao.find(er.getEventID());

            if(e == null){
                return new EventResult("There is no such event " + er.getEventID());
            }

            if(!e.getAssociatedUsername().equals(a.getUserName())){
                return new EventResult("You do not have access to view this event");
            }

            success = true;
            return new EventResult(e);

        }catch(DataAccessException e){
            e.printStackTrace();
            return new EventResult("There was an error getting the Event from EventID " +
                    er.getEventID() + " " + e.getMessage());
        }finally{
            Server.getDatabase().closeConnection(success);
        }
    }

    /**
     * Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth token.
     * @param er contains the authToken of the current user to find all of the events for their family members
     * @return the result of the search
     */
    public MultiEventResult getEvents(EventsRequest er){
        boolean success = false;
        try{
            Server.getDatabase().openConnection();

            EventDAO edao = new EventDAO(Server.getDatabase().getConnection());
            AuthTokenDAO adao = new AuthTokenDAO(Server.getDatabase().getConnection());

            AuthToken a = adao.find(er.getAuthToken());

            if(a == null){
                return new MultiEventResult("You are not logged in, please log in");
            }

            ArrayList<Event> e = edao.findManyByAUsername(a.getUserName());

            success = true;
            return new MultiEventResult(e.toArray(new Event[0]));

        }catch(DataAccessException e){
            e.printStackTrace();
            return new MultiEventResult("There was an error getting the Events from AuthTok " +
                    er.getAuthToken() + " " + e.getMessage());
        }finally{
            Server.getDatabase().closeConnection(success);
        }
    }
}
