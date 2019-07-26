package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.AuthToken;

public class AuthTokenDAO {
    private Connection conn;

    public AuthTokenDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Inserts a AuthToken into the database
     * @param tok the AuthToken to insert into the database
     * @return whether or not the insertion was committed
     * @throws DataAccessException if there is an error inserting the AuthToken into the database
     */
    public boolean insert(AuthToken tok) throws DataAccessException {
        boolean commit = true;
        String sql = "INSERT INTO AuthToks (Username, AuthToken) VALUES(?, ?);";

        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, tok.getUserName());
            stmt.setString(2, tok.getToken());

            stmt.executeUpdate();

        }catch(SQLException e){
            commit = false;
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the AuthTokens database");
        }

        return commit;
    }

    /**
     * Inserts multiple tokens into the database at once
     * @param tokens the tokens to add to the database
     * @return whether or not the changes should be committed to the database
     * @throws DataAccessException if there is an error in connecting to the database
     */
    public boolean insert(ArrayList<AuthToken> tokens) throws DataAccessException {
        //TODO do this function
        return false;
    }

    /**
     * Finds a AuthToken with the given id in the database, returns null if it isn't in the database
     * @param tokenID the id of the token to find
     * @return the token that was found or null if it wasn't found in the database
     * @throws DataAccessException if there is an error searching in the database
     */
    public AuthToken find(String tokenID) throws DataAccessException {
        ResultSet rs = null;

        String sql = "SELECT * FROM AuthToks WHERE AuthToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new AuthToken(rs.getString("Username"), rs.getString("AuthToken"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user " + tokenID);
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
     * Returns all of the AuthTokens associated with a username
     * @param username the username to use for the search
     * @return an arraylist of all of the AuthTokens that are associated with a specific user
     * @throws DataAccessException if there is an error in accessing the database
     */
    public ArrayList<AuthToken> findMany(String username) throws DataAccessException {
        ResultSet rs = null;
        ArrayList<AuthToken> authToks = new ArrayList<>();

        String sql = "SELECT * FROM AuthToks WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                authToks.add(new AuthToken(rs.getString("Username"), rs.getString("AuthToken")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person " + username);
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return authToks;
    }

    /**
     * Clears all of the AuthToks out of the database associated with the given user
     * @throws DataAccessException if there is an error in accessing the database
     */
    public void clearUser(String username) throws DataAccessException {
        String sql = "DELETE FROM AuthToks WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error deleting all of the rows from the authToks database");
        }
    }

    /**
     * Clears all of the AuthToks out of the database
     * @throws DataAccessException if there is an error in accessing the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM AuthToks";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error deleting all of the rows from the authToks database");
        }
    }
}
