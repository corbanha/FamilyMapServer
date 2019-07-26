package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import handlers.Server;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

public class ClearServiceTest {
    Database db;
    Connection conn;

    @Before
    public void setUp() throws Exception {
        //and a new event with random data
        db = Server.getDatabase();

        db.openConnection();

        db.deleteTables();
        db.createTables();

        conn = db.getConnection();
    }

    @After
    public void tearDown() throws Exception{
        db.closeConnection(false);
    }

    @Test
    public void clearFail() throws Exception{

        Person compareTest = null;

        try{
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            UserDAO uDao = new UserDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            aDao.insert(new AuthToken("username", "AuthToken"));
            pDao.insert(new Person("PersonID", "username", "Corban",
                    "Anderson", "m", null, null, null));
            uDao.insert(new User("username", "123456", "corbean@aldsjf",
                    "Corban", "Anderson", "m","PersonID"));
            eDao.insert(new Event("EventID", "username", "PersonID", "234,34", "234,34", "USA", "San Jose", "Birth", 1235));

            compareTest = pDao.find("PersonID");
            assertNotNull(compareTest);

            String message = ClearService.clear().getMessage();

            compareTest = pDao.find("PersonID1");

        }catch(DataAccessException e){

        }

        assertNull(compareTest);
    }
}
