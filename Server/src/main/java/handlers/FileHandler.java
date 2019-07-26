package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {

    /**
     * Handles the given httpExchange when the client sends a generic request for any given file
     * @param httpExchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        URI requestURI = httpExchange.getRequestURI();
        String query = requestURI.getQuery();
        String uriPath = requestURI.getPath();
        String requestMethod = httpExchange.getRequestMethod().toUpperCase();

        try{
            if(requestMethod.equals("GET")){
                if(uriPath.equals("/")){
                    uriPath = "/index.html";
                }

                uriPath = "web" + uriPath;

                System.out.println("Handling Request: " + uriPath);

                File file = new File(uriPath);

                if(file.exists()){
                    if(file.canRead()){
                        httpExchange.sendResponseHeaders(200, 0);
                        OutputStream os = httpExchange.getResponseBody();
                        Files.copy(file.toPath(), os);
                        os.close();
                    }else{
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_FORBIDDEN, 0);
                        httpExchange.getResponseBody().close();
                    }
                }else{
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    OutputStream os = httpExchange.getResponseBody();
                    Files.copy(new File("web/HTML/404.html").toPath(), os);
                    os.close();
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
