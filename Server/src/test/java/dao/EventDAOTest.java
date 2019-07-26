package dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;

import model.Event;

import static org.junit.Assert.*;

public class EventDAOTest{
    Database db;
    Connection conn;
    Event bestEvent;
    EventDAO eDao;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        //and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                "Biking_Around", 2016);

        db.openConnection();

        db.deleteTables();
        db.createTables();

        conn = db.getConnection();
        eDao = new EventDAO(conn);
    }

    @After
    public void tearDown() throws Exception{
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws Exception {

        Event compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO
            eDao.insert(bestEvent);
            //So lets use a find function to get the event that we just put in back out
            compareTest = eDao.find(bestEvent.getEventID());
        } catch (DataAccessException e) {

        }

        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        //lets do this test again but this time lets try to make it fail
        boolean didItWork = true;
        try {
            //if we call the function the first time it will insert it successfully
            eDao.insert(bestEvent);
            eDao.insert(bestEvent);
        } catch (DataAccessException e) {
            didItWork = false;
        }

        assertFalse(didItWork);
    }

    @Test
    public void queryMultiUsernamePass() throws Exception {
        //We'll test getting multiple events back for one username

        ArrayList<Event> compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            eDao.insert(new Event("Biking_123A", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123B", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around2", 2016));
            eDao.insert(new Event("SomethingRandom", "Bob", "Bob123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123C", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around3", 2016));

            //So lets use a find function to get the event that we just put in back out
            compareTest = eDao.findManyByAUsername(bestEvent.getAssociatedUsername());
        } catch (DataAccessException e) {
        }

        assertNotNull(compareTest);
        assertEquals(3, compareTest.size());
    }

    @Test
    public void queryMultiPersonIDPass() throws Exception {
        //We'll test getting multiple events back for one username

        ArrayList<Event> compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            eDao.insert(new Event("Biking_123A", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123B", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around2", 2016));
            eDao.insert(new Event("SomethingRandom", "Bob", "Bob123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123C", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around3", 2016));

            //So lets use a find function to get the event that we just put in back out
            compareTest = eDao.findManyByPersonID(bestEvent.getPersonID());
        } catch (DataAccessException e) {
        }

        assertNotNull(compareTest);
        assertEquals(3, compareTest.size());
    }

    @Test
    public void queryPass() throws Exception{
        //We'll test getting multiple events back for one username

        Event compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO
            eDao.insert(bestEvent);

            //So lets use a find function to get the event that we just put in back out
            compareTest = eDao.find(bestEvent.getEventID());
        } catch (DataAccessException e) {
        }
        assertEquals(compareTest, bestEvent);
    }

    @Test
    public void queryFail() throws Exception{
        //We'll test getting multiple events back for one username

        Event compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            eDao.insert(bestEvent);

            //So lets use a find function to get the event that we just put in back out
            compareTest = eDao.find("BadID");
        } catch (DataAccessException e) {
        }

        assertNull(compareTest);
    }

    @Test
    public void queryMultiPersonIDFail() throws Exception{
        //We'll test getting multiple events back for one username

        ArrayList<Event> compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            eDao.insert(new Event("Biking_123A", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123B", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around2", 2016));
            eDao.insert(new Event("SomethingRandom", "Bob", "Bob123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123C", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around3", 2016));

            //So lets use a find function to get the event that we just put in back out
            compareTest = eDao.findManyByPersonID("BadPersonID");
        } catch (DataAccessException e) {
        }

        assertNotNull(compareTest);
        assertEquals(compareTest.size(), 0);
    }

    @Test
    public void queryMultiUsernameFail() throws Exception{
        //We'll test getting multiple events back for one username

        ArrayList<Event> compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            eDao.insert(new Event("Biking_123A", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123B", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around2", 2016));
            eDao.insert(new Event("SomethingRandom", "Bob", "Bob123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123C", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around3", 2016));

            //So lets use a find function to get the event that we just put in back out
            compareTest = eDao.findManyByAUsername("BadUsername");
        } catch (DataAccessException e) {
        }

        assertNotNull(compareTest);
        assertEquals(0, compareTest.size());
    }

    @Test
    public void deletePass() throws Exception{
        try {
            //Let's get our connection and make a new DAO

            eDao.insert(new Event("Biking_123A", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123B", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around2", 2016));
            eDao.insert(new Event("SomethingRandom", "Bob", "Bob123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123C", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around3", 2016));

            assertNotNull(eDao.find("Biking_123A"));
            assertNotNull(eDao.find("Biking_123B"));
            assertNotNull(eDao.find("Biking_123C"));
            assertNotNull(eDao.find("SomethingRandom"));

            eDao.clear();

            assertNull(eDao.find("Biking_123A"));
            assertNull(eDao.find("Biking_123B"));
            assertNull(eDao.find("Biking_123C"));
            assertNull(eDao.find("SomethingRandom"));
            //So lets use a find function to get the event that we just put in back out
        } catch (DataAccessException e) {
        }
    }

    @Test
    public void deleteFail() throws Exception{
        try {
            //Let's get our connection and make a new DAO

            eDao.insert(new Event("Biking_123A", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123B", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around2", 2016));
            eDao.insert(new Event("SomethingRandom", "Bob", "Bob123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));


            assertNotNull(eDao.find("Biking_123A"));
            assertNotNull(eDao.find("Biking_123B"));
            assertNull(eDao.find("Biking_123C"));
            assertNotNull(eDao.find("SomethingRandom"));

            eDao.clear();

            assertNull(eDao.find("Biking_123A"));
            assertNull(eDao.find("Biking_123B"));
            assertNull(eDao.find("Biking_123C"));
            assertNull(eDao.find("SomethingRandom"));

            eDao.insert(new Event("Biking_123C", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around3", 2016));

            assertNotNull(eDao.find("Biking_123C"));
        } catch (DataAccessException e) {
        }
    }

    @Test
    public void deleteUserPass() throws Exception{
        try {
            //Let's get our connection and make a new DAO

            eDao.insert(new Event("Biking_123A", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123B", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around2", 2016));
            eDao.insert(new Event("SomethingRandom", "Bob", "Bob123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));

            assertNotNull(eDao.find("Biking_123A"));
            assertNotNull(eDao.find("Biking_123B"));
            assertNull(eDao.find("Biking_123C"));
            assertNotNull(eDao.find("SomethingRandom"));

            eDao.clearUser("Gale");

            assertNull(eDao.find("Biking_123A"));
            assertNull(eDao.find("Biking_123B"));
            assertNotNull(eDao.find("SomethingRandom"));
        } catch (DataAccessException e) {
        }

    }
}
