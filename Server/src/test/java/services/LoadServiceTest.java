package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import handlers.Server;
import model.Event;
import model.Person;
import model.User;
import services.requests.LoadRequest;
import services.requests.LoginRequest;
import services.results.LoadResult;
import services.results.LoginResult;

import static junit.framework.TestCase.assertEquals;

public class LoadServiceTest {


    @Before
    public void setUp() throws Exception {
        Server.getDatabase().openConnection();
    }

    @After
    public void tearDown() throws Exception{
        Server.getDatabase().closeConnection(false);
    }

    @Test
    public void getLoadFail() throws Exception{

        //We'll try to log in a user that hasn't been created yet

        LoadResult lr = LoadService.load(new LoadRequest(new User[]{}, new Person[]{}, new Event[]{}));

        assertEquals("Successfully added 0 users, 0 persons, and 0 events to the database", lr.getMessage());

    }
}