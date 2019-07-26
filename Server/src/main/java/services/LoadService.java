package services;

import dao.DataAccessException;
import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import handlers.Server;
import services.requests.LoadRequest;
import services.results.LoadResult;

public class LoadService extends Service {

    /**
     * Loads specific information from the database
     * @param r the LoadRequest to be filled
     * @return the result of trying to perform the load operation
     */
    public static LoadResult load(LoadRequest r){
        boolean success = false;
        try{

            ClearService.clear();

            Server.getDatabase().openConnection();

            UserDAO udao = new UserDAO(Server.getDatabase().getConnection());
            udao.insert(r.getUsers());

            EventDAO edao = new EventDAO(Server.getDatabase().getConnection());
            edao.insert(r.getEvents());

            PersonDAO pdao = new PersonDAO(Server.getDatabase().getConnection());
            pdao.insert(r.getPersons());

            success = true;
            return new LoadResult("Successfully added " + r.getUsers().length + " users, " +
                    r.getPersons().length + " persons, and " + r.getEvents().length +
                    " events to the database");
        }catch(DataAccessException e){
            e.printStackTrace();
            return new LoadResult("There was an error clearing the database: " + e.getMessage());
        }finally{
            Server.getDatabase().closeConnection(success);
        }
    }
}
