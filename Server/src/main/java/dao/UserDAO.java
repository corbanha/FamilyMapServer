package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.crypto.Data;

import model.User;

public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Inserts a user into the database
     * @param user the user to insert into the database
     * @return whether or not the insertion was committed
     * @throws DataAccessException if there is an error inserting the user into the database
     */
    public boolean insert(User user) throws DataAccessException {
        boolean commit = true;
        String sql = "INSERT INTO Users (Username, Password, Email, FirstName, LastName," +
                "Gender, PersonID) VALUES(?, ?, ?, ?, ?, ?, ?);";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();

        }catch(SQLException e){
            commit = false;
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }

        return commit;
    }

    /**
     * Inserts many users into the database
     * @param users the users to insert into the database
     * @return whether or not the operation was committed
     * @throws DataAccessException if there was an error in adding the users
     */
    public boolean insert(User[] users) throws DataAccessException {
        boolean commit = true;
        for(int i = 0, len = users.length; i < len; i++){
            commit = commit && insert(users[i]);
        }
        return commit;
    }

    /**
     * Finds a user with the given id in the database, returns null if they aren't in the database
     * @param username the id of the user to find
     * @return the user that was found or null if they weren't found in the database
     * @throws DataAccessException if there is an error searching in the database
     */
    public User find(String username) throws DataAccessException {
        ResultSet rs = null;

        String sql = "SELECT * FROM Users WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"), rs.getString("PersonID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user " + username);
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
     * Clears all of the Users out of the database
     * @throws DataAccessException if there is an error in accessing the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error deleting all of the rows from the user database");
        }
    }
}
