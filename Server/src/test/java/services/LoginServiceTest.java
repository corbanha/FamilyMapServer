package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.DataAccessException;
import dao.EventDAO;
import dao.PersonDAO;
import handlers.Server;
import model.Event;
import model.Person;
import services.requests.LoginRequest;
import services.requests.PersonRequest;
import services.requests.RegisterRequest;
import services.results.LoginResult;
import services.results.PersonResult;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertEquals;

public class LoginServiceTest {

    @Before
    public void setUp() throws Exception {
        Server.getDatabase().openConnection();
    }

    @After
    public void tearDown() throws Exception{
        Server.getDatabase().closeConnection(false);
    }

    @Test
    public void getLoginFail() throws Exception{

        //We'll try to log in a user that hasn't been created yet

        LoginResult lr = LoginService.login(new LoginRequest("Username", "123456"));

        assertEquals("", lr.getUserName());

    }
}