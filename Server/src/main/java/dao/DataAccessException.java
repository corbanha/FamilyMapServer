package dao;

/**
 * A DataAccessException, is thrown to cover the SQL exceptions that may be thrown
 */

public class DataAccessException extends Exception {
    DataAccessException(String message)
    {
        super(message);
    }

    DataAccessException()
    {
        super();
    }
}
