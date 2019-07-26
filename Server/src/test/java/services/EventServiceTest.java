package services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dao.DataAccessException;
import dao.EventDAO;
import handlers.Server;
import model.Event;
import services.requests.EventRequest;
import services.results.EventResult;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertEquals;


public class EventServiceTest {
    EventService es;
    EventDAO eDao;

    @Before
    public void setUp() throws Exception {
        Server.getDatabase().openConnection();
        eDao = new EventDAO(Server.getDatabase().openConnection());
        es = new EventService();
    }

    @After
    public void tearDown() throws Exception{
        Server.getDatabase().closeConnection(false);
    }

    @Test
    public void getEventPass() throws Exception{
        try {
            //Let's get our connection and make a new DAO

            eDao.insert(new Event("Biking_123A", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123B", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around2", 2016));
            eDao.insert(new Event("SomethingRandom", "Bob", "Bob123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123C", "Gale", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));
            eDao.insert(new Event("Biking_123D", "Sally", "Gale123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around2", 2016));
            eDao.insert(new Event("Biking_123E", "Jill", "Bob123A", "10.3", "10.3", "Japan", "Ushiku",
                    "Biking_Around", 2016));

            EventResult er = es.getEvent(new EventRequest("Biking_123A", "Auth_tok"));

            assertNotNull(er.getEvent());
            assertEquals(er.getEvent().getEventID(), "Biking_123A");

        } catch (DataAccessException e) {
        }

    }
}
