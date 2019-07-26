package dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;

import model.Person;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class PersonDAOTest {

    Database db;
    Connection conn;
    Person bestPerson;
    PersonDAO pDao;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        //and a new event with random data
        bestPerson = new Person("PersonID-1234", "Username",
                "Corban", "Anderson", "m", null, null,null);

        db.openConnection();

        db.deleteTables();
        db.createTables();

        conn = db.getConnection();
        pDao = new PersonDAO(conn);
    }

    @After
    public void tearDown() throws Exception{
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws Exception {

        Person compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO
            pDao.insert(bestPerson);
            //So lets use a find function to get the event that we just put in back out
            compareTest = pDao.find(bestPerson.getPersonID());
        } catch (DataAccessException e) {

        }

        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws Exception {

        boolean didItWork = true;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO
            pDao.insert(bestPerson);
            pDao.insert(bestPerson);
        } catch (DataAccessException e) {
            didItWork = false;
        }

        assertFalse(didItWork);
    }

    @Test
    public void queryMultiUsernamePass() throws Exception {
        //We'll test getting multiple events back for one username

        ArrayList<Person> compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            pDao.insert(new Person("PersonID-1", "Username1",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-2", "Username",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-3", "Username",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-4", "Username",
                    "Corban", "Anderson", "m", null, null,null));

            //So lets use a find function to get the event that we just put in back out
            compareTest = pDao.findMany(bestPerson.getAssociatedUsername());
        } catch (DataAccessException e) {
        }

        assertNotNull(compareTest);
        assertEquals(3, compareTest.size());
    }
    
    @Test
    public void queryFail() throws Exception {
        //We'll test getting multiple events back for one username

        Person compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            pDao.insert(bestPerson);

            //So lets use a find function to get the event that we just put in back out
            compareTest = pDao.find("BadID");
        } catch (DataAccessException e) {
        }

        assertNull(compareTest);
    }

    @Test
    public void queryMultiUsernameFail() throws Exception{
        //We'll test getting multiple events back for one username

        ArrayList<Person> compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            pDao.insert(new Person("PersonID-1", "Username1",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-2", "Username",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-3", "Username",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-4", "Username",
                    "Corban", "Anderson", "m", null, null,null));

            //So lets use a find function to get the event that we just put in back out
            compareTest = pDao.findMany("BadUsername");
        } catch (DataAccessException e) {
        }

        assertNotNull(compareTest);
        assertEquals(0, compareTest.size());
    }

    @Test
    public void deletePass() throws Exception{
        try {
            //Let's get our connection and make a new DAO

            pDao.insert(new Person("PersonID-1", "Username1",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-2", "Username",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-3", "Username",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-4", "Username",
                    "Corban", "Anderson", "m", null, null,null));

            assertNotNull(pDao.find("PersonID-1"));
            assertNotNull(pDao.find("PersonID-2"));
            assertNotNull(pDao.find("PersonID-3"));
            assertNotNull(pDao.find("PersonID-4"));

            pDao.clear();

            assertNull(pDao.find("PersonID-1"));
            assertNull(pDao.find("PersonID-2"));
            assertNull(pDao.find("PersonID-3"));
            assertNull(pDao.find("PersonID-4"));
            //So lets use a find function to get the event that we just put in back out
        } catch (DataAccessException e) {
        }
    }

    @Test
    public void deleteUserPass() throws Exception{
        try {
            //Let's get our connection and make a new DAO

            pDao.insert(new Person("PersonID-1", "Username1",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-2", "Username",
                    "Corban", "Anderson", "m", null, null,null));

            pDao.insert(new Person("PersonID-3", "Username",
                    "Corban", "Anderson", "m", null, null,null));


            assertNotNull(pDao.find("PersonID-1"));
            assertNotNull(pDao.find("PersonID-2"));
            assertNotNull(pDao.find("PersonID-3"));
            assertNull(pDao.find("PersonID-4"));

            pDao.clearUser("Username");

            assertNull(pDao.find("PersonID-2"));
            assertNull(pDao.find("PersonID-3"));
            assertNotNull(pDao.find("PersonID-1"));
        } catch (DataAccessException e) {
        }

    }
    
}
