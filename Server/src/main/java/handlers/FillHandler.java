package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import services.FillService;
import services.PersonService;
import services.requests.FillRequest;

public class FillHandler implements HttpHandler {

    /**
     * Handles the given httpExchange when the client sends a /fill request
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
                PersonService ps = new PersonService();

                while(uriPath.endsWith("/"))
                    uriPath = uriPath.substring(0, uriPath.length() - 1);

                if(uriPath.startsWith("/fill/")){

                    uriPath = uriPath.substring(6);

                    String username;
                    int gens = 4;
                    if(uriPath.contains("/")){
                        username = uriPath.substring(0, uriPath.indexOf("/"));
                        uriPath = uriPath.substring(uriPath.indexOf("/") + 1);

                        try{
                            gens = Integer.parseInt(uriPath);
                        }catch(Exception e){}
                    }else{
                        username = uriPath;
                    }

                    FillService fs = new FillService();

                    String response = fs.fill(new FillRequest(username, gens)).getMessage();

                    String respData = "{ \"message\": \"" + response + "\"}";

                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, respData.getBytes().length);

                    OutputStream respBody = httpExchange.getResponseBody();
                    respBody.write(respData.getBytes());
                    respBody.close();
                }else{
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
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
