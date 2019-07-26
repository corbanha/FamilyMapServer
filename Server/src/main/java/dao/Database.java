package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * A Database with a connection that can be given to A Database Access Object
 */
public class Database {
    private Connection conn;

    static {
        try {
            //This is how we set up the driver for our database
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions

    /**
     * Open a connection to the database, this needs to be done before any operations or DAO's
     * can connect to the database
     * @return a connection object which is then given to DAO's
     * @throws DataAccessException if there is an error connecting to the database
     */
    public Connection openConnection() throws DataAccessException {
        try {
            //The Structure for this Connection is driver:language:path
            //The pathing assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }
        return conn;
    }

    //When we are done manipulating the database it is important to close the connection. This will
    //End the transaction and allow us to either commit our changes to the database or rollback any
    //changes that were made before we encountered a potential error.

    //IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
    //DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
    //OR PROBLEMS YOU ENCOUNTER

    /**
     * Close the connection to the database. Make sure that this is being called, if it is called
     * when the connection is already opened then the database may lock up
     * @param commit whether or not to commit the changes to the database that happened during this
     *               transaction
     * @throws DataAccessException if there is an error closing the database
     */
    public void closeConnection(boolean commit) {
        if(conn != null){
            try {
                if (commit) {
                    //This will commit the changes to the database
                    conn.commit();
                } else {
                    //If we find out something went wrong, pass a false into closeConnection and this
                    //will rollback any changes we made during this connection
                    conn.rollback();
                }

                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Recreates all of the default tables in the database if they don't already exist
     * @throws DataAccessException if there is an error in creating the tables for the database
     */
    public void createTables() throws DataAccessException {

        try (Statement stmt = conn.createStatement()){
            //First lets open our connection to our database.

            //We pull out a statement from the connection we just established
            //Statements are the basis for our transactions in SQL
            //Format this string to be exactly like a sql create table command

            String[] args = {
                "CREATE TABLE IF NOT EXISTS Users ( ",
                "Username TEXT NOT NULL UNIQUE, ",
                "Password TEXT NOT NULL, ",
                "Email  TEXT NOT NULL, ",
                "FirstName TEXT NOT NULL, ",
                "LastName TEXT NOT NULL, ",
                "Gender TEXT NOT NULL, ",
                "PersonID TEXT NOT NULL UNIQUE, ",
                "PRIMARY KEY (Username), ",
                "FOREIGN KEY (PersonID) REFERENCES Persons(PersonID));",

                "CREATE TABLE IF NOT EXISTS Persons ( ",
                "PersonID TEXT NOT NULL UNIQUE, ",
                "Username TEXT NOT NULL, ",
                "FirstName TEXT NOT NULL, ",
                "LastName TEXT NOT NULL, ",
                "Gender TEXT NOT NULL, ",
                "FatherID TEXT, ",
                "MotherID TEXT, ",
                "SpouseID TEXT, ",
                "PRIMARY KEY (PersonID), ",
                "FOREIGN KEY (FatherID) REFERENCES Persons(PersonID), ",
                "FOREIGN KEY (MotherID) REFERENCES Persons(PersonID), ",
                "FOREIGN KEY (SpouseID) REFERENCES Persons(PersonID)); ",

                "CREATE TABLE IF NOT EXISTS Events ( ",
                "EventID TEXT NOT NULL UNIQUE, ",
                "AUsername TEXT NOT NULL, ",
                "PersonID TEXT NOT NULL, ",
                "Latitude TEXT NOT NULL, ",
                "Longitude TEXT NOT NULL, ",
                "Country TEXT NOT NULL, ",
                "City  TEXT NOT NULL, ",
                "EventType TEXT NOT NULL, ",
                "Year  INTEGER NOT NULL, ",
                "PRIMARY KEY (EventID), ",
                "FOREIGN KEY (AUsername) REFERENCES Users(Username), ",
                "FOREIGN KEY (PersonID) REFERENCES Persons(PersonID));",

                "CREATE TABLE IF NOT EXISTS AuthToks ( ",
                "Username TEXT NOT NULL, ",
                "AuthToken TEXT NOT NULL UNIQUE, ",
                "PRIMARY KEY (AuthToken), ",
                "FOREIGN KEY (Username) REFERENCES Users(Username));"
            };

            StringBuilder sql = new StringBuilder();

            for(String str : args){
                sql.append(str);
            }

            stmt.executeUpdate(sql.toString());
            //if we got here without any problems we successfully created the table and can commit
        } catch (SQLException e) {
            //if our table creation caused an error, we can just not commit the changes that did happen
            e.printStackTrace();
            throw new DataAccessException("SQL Error encountered while creating tables");
        }

    }

    /**
     * Clears all of the tables in the database
     * @throws DataAccessException if there is an error in clearing the tables in the database
     */
    public void clearTables() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Users; DELETE FROM Persons; DELETE FROM Events; DELETE FROM AuthToks;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    /**
     * Deletes all of the tables from the database if they exist
     * @throws DataAccessException if there is an error in deleting the tables in the database
     */
    public void deleteTables() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DROP TABLE IF EXISTS Users; DROP TABLE IF EXISTS Persons; DROP TABLE IF EXISTS Events; DROP TABLE IF EXISTS AuthToks;";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    /**
     * Returns a connection to the database, for use by the DAO classes
     * @return a connection to the database
     */
    public Connection getConnection(){
        return conn;
    }
}

