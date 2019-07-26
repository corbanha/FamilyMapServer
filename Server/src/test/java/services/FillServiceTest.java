package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import handlers.Server;
import services.requests.FillRequest;
import services.requests.LoginRequest;
import services.requests.RegisterRequest;
import services.results.FillResult;
import services.results.LoginResult;

import static junit.framework.TestCase.assertEquals;

public class FillServiceTest {

    FillService fs;

    @Before
    public void setUp() throws Exception {
        Server.getDatabase().openConnection();
        fs = new FillService();
    }

    @After
    public void tearDown() throws Exception{
        Server.getDatabase().closeConnection(false);
    }

    @Test
    public void getFillFail() throws Exception{

        //We'll try to log in a user that hasn't been created yet

        FillResult fr = fs.fill(new FillRequest("Username", 4));

        assertEquals("There was an error filling the user Username Error encountered while finding user Username", fr.getMessage());
    }
}

