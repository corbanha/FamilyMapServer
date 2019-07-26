package com.corbanha.familymapserver.net;

import android.widget.Toast;

import com.corbanha.familymapserver.model.Event;
import com.corbanha.familymapserver.model.Model;
import com.corbanha.familymapserver.model.Person;
import com.corbanha.familymapserver.model.User;
import com.corbanha.familymapserver.model.results.MultiEventResult;
import com.corbanha.familymapserver.model.results.MultiPersonResult;
import com.corbanha.familymapserver.ui.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerProxy {

    // Sends a GET Request
    public static boolean getUserData(Model.SignInDetails signInDetails, String authToken, String personID) {

        Person[] persons = null;
        Event[] events = null;

        HttpURLConnection http = null;

        try {
            //Get all of the persons
            URL url = new URL("http://" + signInDetails.serverHost + ":" + signInDetails.serverPort + "/person");

            http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false); //wont contain a request body

            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json"); //set server response to JSON

            // Connect to the server and send the HTTP request
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);

                respBody.close();

                Gson gson = new GsonBuilder().create();
                MultiPersonResult mpr = gson.fromJson(respData, MultiPersonResult.class);
                persons = mpr.getPersons();
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                return false;
            }

            http.disconnect();

            //Now get the events

            url = new URL("http://" + signInDetails.serverHost + ":" + signInDetails.serverPort + "/event");

            http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false); //wont contain a request body

            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json"); //set server response to JSON

            // Connect to the server and send the HTTP request
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                respBody.close();

                Gson gson = new GsonBuilder().create();
                MultiEventResult mpr = gson.fromJson(respData, MultiEventResult.class);
                events = mpr.getEvents();

                System.out.println("SUCCESSFULLY GOT THE EVENTS");
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                return false;
            }

            System.out.println("PERSONID: " + personID);
            Model.getInstance().setBulkData(signInDetails, persons, events);
            Model.getInstance().setUser(Model.getInstance().getPerson(personID));
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(http != null){
                http.disconnect();
            }
        }
        return false;
    }


    public static Object[] registerUser(String serverHost, int serverPort, User user) {

        HttpURLConnection http = null;

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);	// There is a request body

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            // This is the JSON string we will send in the HTTP request body
            String reqData =
                    "{" +
                            "\"userName\": \"" + user.getUserName() + "\"," +
                            "\"password\": \"" + user.getPassword() + "\"," +
                            "\"email\": \"" + user.getEmail() + "\"," +
                            "\"firstName\": \"" + user.getFirstName() + "\"," +
                            "\"lastName\": \"" + user.getLastName() + "\"," +
                            "\"gender\": \"" + user.getGender() + "\"" +
                            "}";

            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // The HTTP response status code indicates success,
                // so print a success message
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                respBody.close();

                Gson gson = new Gson();
                ResultContainer rc = gson.fromJson(respData, ResultContainer.class);

                if(rc.authToken == null){
                    return new Object[]{false, rc.message};
                }

                //Now we'll bring in their data
                if(getUserData(new Model.SignInDetails(serverHost, serverPort, user.getUserName(),
                        user.getPassword()), rc.authToken, rc.personID) && Model.getInstance().getUser() != null){
                    return new Object[]{true, "Welcome, " + Model.getInstance().getUser().getFirstName() + " " + Model.getInstance().getUser().getLastName()};
                }else{
                    return new Object[]{false, "There was an error Registering you"};
                }
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());
                return new Object[]{false, "Error reaching the server. Code: " + http.getResponseCode()};
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }finally{
            if(http != null){
                http.disconnect();
            }
        }
        return new Object[]{false, "Error Registering user"};
    }

    public static Object[] loginUser(String serverHost, int serverPort, User user) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);	// There is a request body

            http.addRequestProperty("Accept", "application/json");

            http.connect();

            // This is the JSON string we will send in the HTTP request body
            String reqData =
                    "{" +
                            "\"userName\": \"" + user.getUserName() + "\"," +
                            "\"password\": \"" + user.getPassword() + "\"" +
                            "}";

            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                System.out.print(respData);

                Gson gson = new Gson();
                ResultContainer rc = gson.fromJson(respData, ResultContainer.class);

                if(rc.authToken == null){
                    return new Object[]{false, rc.message};
                }

                //Now we'll bring in their data
                if(getUserData(new Model.SignInDetails(serverHost, serverPort, user.getUserName(),
                        user.getPassword()), rc.authToken, rc.personID) && Model.getInstance().getUser() != null){
                    return new Object[]{true, "Welcome back " + Model.getInstance().getUser().getFirstName() + " " + Model.getInstance().getUser().getLastName()};
                }else{
                    return new Object[]{false, "There was an error retrieving your data"};
                }
            }
            else {
                return new Object[]{false, "Error: " + http.getResponseMessage()};
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            System.out.println("BIGGER ERROR");
            e.printStackTrace();
        }
        return new Object[]{false, "There was an error logging you in"};
    }




    /*
		The readString method shows how to read a String from an InputStream.
	*/
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
		The writeString method shows how to write a String to an OutputStream.
	*/
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }


    class ResultContainer{
        String authToken;
        String userName;
        String personID;
        Person person;
        String message;
    }
}
