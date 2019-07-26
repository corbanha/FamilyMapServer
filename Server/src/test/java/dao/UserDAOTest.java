package dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import model.User;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class UserDAOTest {
    Database db;
    User user;
    Connection conn;
    UserDAO uDao;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        //and a new event with random data
        user = new User("corbanha", "12345", "corban@corbanha.com",
                "Corban", "Anderson", "m", "person12");

        db.openConnection();

        db.deleteTables();
        db.createTables();

        conn = db.getConnection();
        uDao = new UserDAO(conn);
    }

    @After
    public void tearDown() throws Exception{
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws Exception {
        User compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            uDao.insert(user);
            //So lets use a find function to get the event that we just put in back out
            compareTest = uDao.find(user.getUserName());
        } catch (DataAccessException e) {
        }

        assertNotNull(compareTest);
        assertEquals(user, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            //if we call the function the first time it will insert it successfully
            uDao.insert(user);
            uDao.insert(user); //it should get angry the second time
        } catch (DataAccessException e) {
            didItWork = false;
        }

        assertFalse(didItWork);
    }

    @Test
    public void queryPass() throws Exception {
        User compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            uDao.insert(user);

            //So lets use a find function to get the event that we just put in back out
            compareTest = uDao.find(user.getUserName());
        } catch (DataAccessException e) {
        }

        assertEquals(compareTest, user);
    }

    @Test
    public void queryFail() throws Exception {
        User compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            uDao.insert(user);

            //So lets use a find function to get the event that we just put in back out
            compareTest = uDao.find("BadID");
        } catch (DataAccessException e) {
        }

        assertNull(compareTest);
    }

    @Test
    public void deletePass() throws Exception {
        try {
            //Let's get our connection and make a new DAO

            for(int i = 1; i < 5; i++){
                uDao.insert(new User("corbanha" + i, "12345",
                        "corbeanha@gmial.co" + i, "Corban", "Anderson",
                        "m", "person1"));
            }

            for(int i = 1; i < 5; i++){
                assertNotNull(uDao.find("user" + i));
            }


            uDao.clear();

            for(int i = 1; i < 5; i++){
                assertNull(uDao.find("user" + i));
            }

        } catch (DataAccessException e) {
        }
    }

    @Test
    public void deleteFail() throws Exception {
        try {
            //Let's get our connection and make a new DAO

            for(int i = 1; i < 5; i++){
                uDao.insert(new User("corbanha" + i, "12345",
                        "corbeanha@gmial.co" + i, "Corban", "Anderson",
                        "m", "person1"));
            }

            for(int i = 1; i < 5; i++){
                assertNotNull(uDao.find("user" + i));
            }

            assertNull(uDao.find("user5"));

            uDao.clear();

            for(int i = 1; i < 5; i++){
                assertNull(uDao.find("user" + i));
            }

            uDao.insert(new User("corbanha5", "12345",
                    "corbeanha@gmial.co5", "Corban", "Anderson",
                    "m", "person1"));

            assertNotNull("user5");

        } catch (DataAccessException e) {
        }
    }
}
