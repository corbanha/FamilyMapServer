package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Event;

public class EventDAO {
    private Connection conn;

    public EventDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts an event into the database
     * @param event the event to insert into the database
     * @return whether or not the change was committed to the database
     * @throws DataAccessException if there is an error in inserting the event into the database
     */
    public boolean insert(Event event) throws DataAccessException {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Events (EventID, AUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?);";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setString(4, event.getLatitude());
            stmt.setString(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            commit = false;
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the Events database");
        }

        return commit;
    }

    /**
     * Adds many events into the database at once
     * @param events the events to add into the database
     * @return whether or not the changes should be committed to the database
     * @throws DataAccessException if there is an error in connecting to the database
     */
    public boolean insert(Event[] events) throws DataAccessException{
        boolean commit = true;
        for(int i = 0, len = events.length; i < len; i++){
            commit = commit && insert(events[i]);
        }
        return commit;
    }

    /**
     * Finds an event with the given ID in the database or null if the event is not found
     * @param eventID the id of the event to find in the database
     * @return the event that was found in the database or null if the event was not found
     * @throws DataAccessException if there is an error in searching the database
     */
    public Event find(String eventID) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ? ;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new Event(rs.getString("EventID"), rs.getString("AUsername"),
                        rs.getString("PersonID"), rs.getString("Latitude"), rs.getString("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event " + eventID);
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Finds all of the events that are associated with a given Person
     * @param personID the user to search for
     * @return an ArrayList that contains all of the events for a given person
     * @throws DataAccessException if there is an error in accessing the database
     */
    public ArrayList<Event> findManyByPersonID(String personID) throws DataAccessException {
        ResultSet rs = null;
        ArrayList<Event> persons = new ArrayList<>();

        String sql = "SELECT * FROM Events WHERE PersonID = ? ;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                persons.add(new Event(rs.getString("EventID"), rs.getString("AUsername"),
                        rs.getString("PersonID"), rs.getString("Latitude"), rs.getString("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person " + personID);
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return persons;
    }

    /**
     * Finds all of the events that are associated with a given Person
     * @param aUsername the user to search for
     * @return an ArrayList that contains all of the events for a given person
     * @throws DataAccessException if there is an error in accessing the database
     */
    public ArrayList<Event> findManyByAUsername(String aUsername) throws DataAccessException {
        ResultSet rs = null;
        ArrayList<Event> persons = new ArrayList<>();

        String sql = "SELECT * FROM Events WHERE AUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                persons.add(new Event(rs.getString("EventID"), rs.getString("AUsername"),
                        rs.getString("PersonID"), rs.getString("Latitude"), rs.getString("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person " + aUsername);
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return persons;
    }

    /**
     * Clears all of the Event rows that are associated with the given username
     * @param username
     * @throws DataAccessException
     */
    public void clearUser(String username) throws DataAccessException{
        String sql = "DELETE FROM Events WHERE AUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error deleting specific rows from the Events database" +
                    e.getMessage());
        }
    }

    /**
     * Clears all of the events out of the database
     * @throws DataAccessException if there is an error in accessing the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Events";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error deleting all of the rows from the events database");
        }
    }
}
