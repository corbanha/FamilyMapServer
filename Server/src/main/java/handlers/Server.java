package handlers;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

import dao.Database;

public class Server {

    // The maximum number of waiting incoming connections to queue.
    private static final int MAX_WAITING_CONNECTIONS = 12;

    private static Database database;
    static{
        System.out.println("Connecting to Database");
        database = new Database();
    }

    private HttpServer server;

    public static void main(String[] args) {
        String portNumber = "1200"; //args[0];
        new Server().run(portNumber);
    }

    private void run(String portNumber) {

        System.out.println("Initializing HTTP Server on Port " + portNumber);

        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        server.setExecutor(null);

        System.out.println("Creating contexts");

        server.createContext("/", new FileHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill", new FillHandler());

        server.createContext("/user/login", new LoginHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/person", new PersonHandler());

        System.out.println("Starting server");
        server.start();

        System.out.println("Server started");
    }

    /**
     * Gets a database object
     * @return a pointer to the database
     */
    public static Database getDatabase(){
        return database;
    }
}