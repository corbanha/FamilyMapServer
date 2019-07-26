package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import dao.DataAccessException;
import services.PersonService;
import services.requests.PersonRequest;
import services.requests.PersonsRequest;
import services.results.MultiPersonResult;
import services.results.Result;

public class PersonHandler implements HttpHandler {

    /**
     * Handles the given httpExchange when the client sends a /person request
     * @param httpExchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        String uriPath = requestURI.getPath();
        String requestMethod = httpExchange.getRequestMethod().toUpperCase();

        System.out.println("Handling Request: " + uriPath + " q: " + query + " Type: " + requestMethod);

        try{
            if(requestMethod.equals("GET") || requestMethod.equals("POST")){
                Headers reqHeaders = httpExchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    PersonService ps = new PersonService();
                    String authKey = reqHeaders.getFirst("Authorization");

                    Result result = null;

                    while(uriPath.endsWith("/"))
                        uriPath = uriPath.substring(0, uriPath.length() - 1);

                    String respData;

                    if(uriPath.toLowerCase().equals("/person")){
                        result = ps.getPersons(new PersonsRequest(authKey));
                    }else{
                        result = ps.getPerson(new PersonRequest(uriPath.substring(8), authKey));

                    }


                    if(result.getMessage().equals("")){
                        respData = new Gson().toJson(result);
                    }else{
                        respData = "{ \"message\": \"" + result.getMessage() + "\"}";
                    }

                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, respData.getBytes().length);

                    OutputStream respBody = httpExchange.getResponseBody();
                    respBody.write(respData.getBytes());
                    respBody.close();

                }else{
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
                    httpExchange.getResponseBody().close();
                }
            }else{
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                httpExchange.getResponseBody().close();
            }
        }catch(IOException e){
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            httpExchange.getResponseBody().close();
            System.out.println("INTERNAL SERVER ERROR " + e.getMessage());
            e.printStackTrace();
        }
    }
}
