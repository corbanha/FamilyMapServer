package dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;


import model.AuthToken;
import model.Person;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class AuthTokenDAOTest {
    Database db;
    Connection conn;
    AuthToken bestAuthTok;
    AuthTokenDAO aDao;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        //and a new event with random data
        bestAuthTok = new AuthToken("corbanha", "Token-1");

        db.openConnection();

        db.deleteTables();
        db.createTables();

        conn = db.getConnection();
        aDao = new AuthTokenDAO(conn);
    }

    @After
    public void tearDown() throws Exception{
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws Exception {

        AuthToken compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO
            aDao.insert(bestAuthTok);
            //So lets use a find function to get the event that we just put in back out
            compareTest = aDao.find(bestAuthTok.getToken());
        } catch (DataAccessException e) {

        }

        assertNotNull(compareTest);
        assertEquals(bestAuthTok, compareTest);
    }

    @Test
    public void insertFail() throws Exception {

        boolean didItWork = true;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO
            aDao.insert(bestAuthTok);
            aDao.insert(bestAuthTok);
        } catch (DataAccessException e) {
            didItWork = false;
        }

        assertFalse(didItWork);
    }

    @Test
    public void queryMultiUsernamePass() throws Exception {
        //We'll test getting multiple events back for one username

        ArrayList<AuthToken> compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            aDao.insert(new AuthToken("corbanha", "Token-1"));
            aDao.insert(new AuthToken("corbanha", "Token-2"));
            aDao.insert(new AuthToken("corbanha", "Token-3"));
            aDao.insert(new AuthToken("corbanha", "Token-4"));


            //So lets use a find function to get the event that we just put in back out
            compareTest = aDao.findMany(bestAuthTok.getUserName());
        } catch (DataAccessException e) {
        }

        assertNotNull(compareTest);
        assertEquals(4, compareTest.size());
    }

    @Test
    public void queryFail() throws Exception {
        //We'll test getting multiple events back for one username

        AuthToken compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            aDao.insert(bestAuthTok);

            //So lets use a find function to get the event that we just put in back out
            compareTest = aDao.find("BadID");
        } catch (DataAccessException e) {
        }

        assertNull(compareTest);
    }

    @Test
    public void queryMultiUsernameFail() throws Exception{
        //We'll test getting multiple events back for one username

        ArrayList<AuthToken> compareTest = null;
        //Let's clear the database as well so any lingering data doesn't affect our tests
        try {
            //Let's get our connection and make a new DAO

            aDao.insert(new AuthToken("corbanha1", "Token-1"));
            aDao.insert(new AuthToken("corbanha2", "Token-2"));
            aDao.insert(new AuthToken("corbanha3", "Token-3"));
            aDao.insert(new AuthToken("corbanha1", "Token-4"));

            //So lets use a find function to get the event that we just put in back out
            compareTest = aDao.findMany("BadUsername");
        } catch (DataAccessException e) {
        }

        assertNotNull(compareTest);
        assertEquals(0, compareTest.size());
    }

    @Test
    public void deletePass() throws Exception{
        try {
            //Let's get our connection and make a new DAO

            aDao.insert(new AuthToken("corbanha1", "Token-1"));
            aDao.insert(new AuthToken("corbanha2", "Token-2"));
            aDao.insert(new AuthToken("corbanha3", "Token-3"));
            aDao.insert(new AuthToken("corbanha4", "Token-4"));

            assertNotNull(aDao.find("Token-1"));
            assertNotNull(aDao.find("Token-2"));
            assertNotNull(aDao.find("Token-3"));
            assertNotNull(aDao.find("Token-4"));

            aDao.clear();

            assertNull(aDao.find("Token-1"));
            assertNull(aDao.find("Token-2"));
            assertNull(aDao.find("Token-3"));
            assertNull(aDao.find("Token-4"));
            //So lets use a find function to get the event that we just put in back out
        } catch (DataAccessException e) {
        }
    }

    @Test
    public void deleteUserPass() throws Exception{
        try {
            //Let's get our connection and make a new DAO

            aDao.insert(new AuthToken("corbanha1", "Token-1"));
            aDao.insert(new AuthToken("corbanha1", "Token-2"));
            aDao.insert(new AuthToken("corbanha2", "Token-3"));


            assertNotNull(aDao.find("Token-1"));
            assertNotNull(aDao.find("Token-2"));
            assertNotNull(aDao.find("Token-3"));
            assertNull(aDao.find("Token-4"));

            aDao.clearUser("corbanha1");

            assertNull(aDao.find("Token-1"));
            assertNull(aDao.find("Token-2"));
            assertNotNull(aDao.find("Token-3"));
        } catch (DataAccessException e) {
        }

    }
}
