package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Person;

public class PersonDAO {
    private Connection conn;

    public PersonDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Inserts a person into the database
     * @param person the person to insert into the database
     * @return whether or not the insertion was committed
     * @throws DataAccessException if there is an error inserting the person into the database
     */
    public boolean insert(Person person) throws DataAccessException {
        boolean commit = true;
        String sql = "INSERT INTO Persons (PersonID, Username, FirstName, LastName, " +
                "Gender, FatherID, MotherID, SpouseID) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();

        }catch(SQLException e){
            commit = false;
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the Persons database");
        }

        return commit;
    }

    /**
     * Adds multiple persons into the database at once
     * @param persons the persons to add into the database
     * @return whether or not the changes should be committed to the database
     * @throws DataAccessException if there is an error in connecting to the database
     */
    public boolean insert(Person[] persons) throws DataAccessException {
        boolean commit = true;
        for(int i = 0, len = persons.length; i < len; i++){
            commit = commit && insert(persons[i]);
        }
        return commit;
    }

    /**
     * Finds a person with the given id in the database, returns null if they aren't in the database
     * @param personID the id of the person to find
     * @return the person that was found or null if they weren't found in the database
     * @throws DataAccessException if there is an error searching in the database
     */
    public Person find(String personID) throws DataAccessException {
        ResultSet rs = null;

        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new Person(rs.getString("PersonID"), rs.getString("Username"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));
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
        return null;
    }

    /**
     * Finds all of the persons associated with a username
     * @param associatedUsername the username in question
     * @return Returns an arraylist of Persons who are associated with the username
     * @throws DataAccessException if there is an error in accessing the database
     */
    public ArrayList<Person> findMany(String associatedUsername) throws DataAccessException {
        ResultSet rs = null;
        ArrayList<Person> persons = new ArrayList<>();

        String sql = "SELECT * FROM Persons WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            rs = stmt.executeQuery();
            while (rs.next()) {
                persons.add(new Person(rs.getString("PersonID"), rs.getString("Username"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person " + associatedUsername);
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
     * Clears all of the Person rows that are associated with the given username
     * @param username
     * @throws DataAccessException
     */
    public void clearUser(String username) throws DataAccessException{
        String sql = "DELETE FROM Persons WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error deleting all of the rows from the person database");
        }
    }

    /**
     * Clears all of the Persons out of the database
     * @throws DataAccessException if there is an error in accessing the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Persons";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error deleting all of the rows from the person database");
        }
    }
}
