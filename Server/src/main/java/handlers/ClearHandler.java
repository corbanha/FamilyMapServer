package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import services.ClearService;

public class ClearHandler implements HttpHandler {

    /**
     * Handles the given httpExchange when the client sends a /clear request
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
            if(requestMethod.equals("POST") || requestMethod.equals("GET")){

                String message = ClearService.clear().getMessage();

                String respData = "{ \"message\": \"" + message + "\"}";

                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, respData.getBytes().length);

                OutputStream respBody = httpExchange.getResponseBody();
                respBody.write(respData.getBytes());
                respBody.close();

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
