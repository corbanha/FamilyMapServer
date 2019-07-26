package services;

import java.util.Random;
import java.util.UUID;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import dao.UserDAO;
import handlers.RegisterHandler;
import handlers.Server;
import model.AuthToken;
import model.Person;
import model.User;
import services.requests.RegisterRequest;
import services.results.RegisterResult;

public class RegisterService extends Service{

    /**
     * Register a new user for the database
     * @param r the given register request
     * @return the result of trying to register the new user
     */
    public static RegisterResult register(RegisterRequest r){
        boolean success = false;
        try{
            Server.getDatabase().openConnection();
            UserDAO udao = new UserDAO(Server.getDatabase().getConnection());
            PersonDAO pdao = new PersonDAO(Server.getDatabase().getConnection());
            AuthTokenDAO adao = new AuthTokenDAO(Server.getDatabase().getConnection());

            User u = new User(r.getUserName(), r.getPassword(), r.getEmail(), r.getFirstName(), r.getLastName(),
                    r.getGender(), "PersonID-" + UUID.randomUUID().toString());

            if(udao.find(r.getUserName()) != null){
                return new RegisterResult("There is already a user with this username registered");
            }

            udao.insert(u);

            AuthToken authTok = new AuthToken(r.getUserName(), "AuthTok-" + UUID.randomUUID().toString());
            adao.insert(authTok);

            Person person = new Person(u.getPersonID(), u.getUserName(), u.getFirstName(),
                    u.getLastName(), u.getGender(), null, null, null);
            pdao.insert(person);

            success = true;
            return new RegisterResult(authTok.getToken(), r.getUserName(), u.getPersonID());

        }catch(DataAccessException e){
            e.printStackTrace();

            return new RegisterResult("There was an error creating registering the new user " + r.getUserName() + " " + e.getMessage());
        }finally{
            Server.getDatabase().closeConnection(success);
        }
    }
}