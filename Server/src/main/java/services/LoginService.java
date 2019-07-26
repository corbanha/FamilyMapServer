package services;

import java.util.UUID;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.UserDAO;
import handlers.Server;
import model.AuthToken;
import model.User;
import services.requests.LoginRequest;
import services.results.LoginResult;

public class LoginService extends Service{

    /**
     * Login the user into the database
     * @param r the LoginRequest
     * @return the result of trying to login the given user
     */
    public static LoginResult login(LoginRequest r){
        boolean success = false;
        try{
            Server.getDatabase().openConnection();

            UserDAO udao = new UserDAO(Server.getDatabase().getConnection());
            AuthTokenDAO adao = new AuthTokenDAO(Server.getDatabase().getConnection());

            User u = udao.find(r.getUserName());

            if(u == null){
                return new LoginResult("There is no such user " + r.getUserName());
            }

            if(u.getPassword().equals(r.getPassword())){
                AuthToken authTok = new AuthToken(r.getUserName(), "AuthTok-" +
                        UUID.randomUUID().toString());
                adao.insert(authTok);

                success = true;
                return new LoginResult(authTok.getToken(), r.getUserName(), u.getPersonID());
            }else{
                return new LoginResult("Invalid Password for user " + r.getUserName());
            }

        }catch(DataAccessException e){
            e.printStackTrace();
            return new LoginResult("There was an error logging in the user " +
                    r.getUserName() + " " + e.getMessage());
        }finally{
            Server.getDatabase().closeConnection(success);
        }
    }
}