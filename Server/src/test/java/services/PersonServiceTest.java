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
import services.requests.PersonRequest;
import services.results.PersonResult;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertEquals;

public class PersonServiceTest {

    PersonService es;
    PersonDAO pDao;

    @Before
    public void setUp() throws Exception {
        Server.getDatabase().openConnection();
        pDao = new PersonDAO(Server.getDatabase().openConnection());
        es = new PersonService();
    }

    @After
    public void tearDown() throws Exception{
        Server.getDatabase().closeConnection(false);
    }

    @Test
    public void getPersonPass() throws Exception{
        try {
            //Let's get our connection and make a new DAO

            pDao.insert(new Person("PersonID-1234", "Username",
                    "Corban", "Anderson", "m", null, null,null));
            pDao.insert(new Person("PersonID-1235", "Username",
                    "Corban", "Anderson", "m", null, null,null));
            pDao.insert(new Person("PersonID-1236", "Username",
                    "Corban", "Anderson", "m", null, null,null));
            pDao.insert(new Person("PersonID-1237", "Username",
                    "Corban", "Anderson", "m", null, null,null));
            pDao.insert(new Person("PersonID-1238", "Username",
                    "Corban", "Anderson", "m", null, null,null));
            pDao.insert(new Person("PersonID-1239", "Username",
                    "Corban", "Anderson", "m", null, null,null));


            PersonResult er = es.getPerson(new PersonRequest("Biking_1234", "Auth_tok"));

            assertNotNull(er.getPerson());
            assertEquals(er.getPerson().getPersonID(), "Biking_1234");

        } catch (DataAccessException e) {
        }

    }
}
