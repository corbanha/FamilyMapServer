package services;

import java.util.ArrayList;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.PersonDAO;
import handlers.Server;
import model.AuthToken;
import model.Person;
import services.requests.PersonRequest;
import services.requests.PersonsRequest;
import services.results.MultiPersonResult;
import services.results.PersonResult;

public class PersonService extends Service {

    /**
     * Returns the single Person object with the specified ID.
     * @param personRequest the personRequest obj of the person ID to search for
     * @return the Result of the search
     */
    public PersonResult getPerson(PersonRequest personRequest){
        boolean success = false;
        try{
            Server.getDatabase().openConnection();

            PersonDAO pdao = new PersonDAO(Server.getDatabase().getConnection());
            AuthTokenDAO adao = new AuthTokenDAO(Server.getDatabase().getConnection());

            AuthToken a = adao.find(personRequest.getAuthToken());

            if(a == null){
                return new PersonResult("You are not logged in, please log in");
            }

            Person p = pdao.find(personRequest.getPersonID());

            if(p == null){
                return new PersonResult("There is no such person " + personRequest.getPersonID());
            }

            if(!p.getAssociatedUsername().equals(a.getUserName())){
                return new PersonResult("You do not have access to view this person");
            }

            success = true;
            return new PersonResult(p);

        }catch(DataAccessException e){
            e.printStackTrace();
            return new PersonResult("There was an error getting the Person from personID " +
                    personRequest.getPersonID() + " " + e.getMessage());
        }finally{
            Server.getDatabase().closeConnection(success);
        }
    }

    /**
     * Returns ALL family members of the current user. The current user is
     * determined from the provided auth token.
     * @param personsRequest the personsRequest obj of the authTok of the user to search for all of
     *                       their persons
     * @return
     */
    public MultiPersonResult getPersons(PersonsRequest personsRequest){
        boolean success = false;
        try{
            Server.getDatabase().openConnection();

            PersonDAO pdao = new PersonDAO(Server.getDatabase().getConnection());
            AuthTokenDAO adao = new AuthTokenDAO(Server.getDatabase().getConnection());

            AuthToken a = adao.find(personsRequest.getAuthToken());

            if(a == null){
                return new MultiPersonResult("You are not logged in, please log in");
            }

            ArrayList<Person> p = pdao.findMany(a.getUserName());

            success = true;
            return new MultiPersonResult(p.toArray(new Person[0]));

        }catch(DataAccessException e){
            e.printStackTrace();
            return new MultiPersonResult("There was an error getting the Persons based on AuthTok " +
                    personsRequest.getAuthToken() + " " + e.getMessage());
        }finally{
            Server.getDatabase().closeConnection(success);
        }
    }
}






































