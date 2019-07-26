package services;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import handlers.Server;
import services.results.ClearResult;

public class ClearService extends Service{

    /**
     * Clears all of the database tables
     * @return the result of trying to perform the request
     */
    public static ClearResult clear(){

        boolean success = false;
        try{
            Server.getDatabase().openConnection();
            new UserDAO(Server.getDatabase().getConnection()).clear();
            new AuthTokenDAO(Server.getDatabase().getConnection()).clear();
            new EventDAO(Server.getDatabase().getConnection()).clear();
            new PersonDAO(Server.getDatabase().getConnection()).clear();

            success = true;
            return new ClearResult("Clear succeeded");
        }catch(DataAccessException e){
            e.printStackTrace();
            return new ClearResult("There was an error clearing the database: " + e.getMessage());
        }finally {
            Server.getDatabase().closeConnection(success);
        }
    }
}