package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.UserDAO;
import handlers.Server;
import model.User;
import services.requests.LoginRequest;
import services.requests.RegisterRequest;
import services.results.LoginResult;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

public class RegisterServiceTest {


    @Before
    public void setUp() throws Exception {
        Server.getDatabase().openConnection();
    }

    @After
    public void tearDown() throws Exception{
        Server.getDatabase().closeConnection(false);
    }

    @Test
    public void getRegisterFail() throws Exception{

        //We'll try to log in a user that hasn't been created yet

        User user = null;

        try{
            RegisterService.register(new RegisterRequest("Username", "123456", "corban@corbanha.com",
                    "Corban", "Anderson", "m"));

            UserDAO udao = new UserDAO(Server.getDatabase().getConnection());
            user = udao.find("BadUsername");


        }catch(Exception e){

        }
        assertNull(user);

    }
}