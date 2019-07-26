package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import model.User;
import services.FillService;
import services.RegisterService;
import services.requests.FillRequest;
import services.requests.RegisterRequest;
import services.results.RegisterResult;

public class RegisterHandler implements HttpHandler {

    /**
     * Handles the given httpExchange when the client sends a /register request
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
        /*System.out.println("-- headers --");
        Headers requestHeaders = httpExchange.getRequestHeaders();
        for(Iterator<Map.Entry<String, List<String>>> iter = requestHeaders.entrySet().iterator(); iter.hasNext();){
            System.out.println(iter.next());
        }*/

        try{

            if(requestMethod.equals("POST")){

                User user = new Gson().fromJson(new InputStreamReader(httpExchange.getRequestBody()), User.class);
                RegisterResult result = RegisterService.register(new RegisterRequest(user.getUserName(),
                        user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getGender()));

                String respData;
                if(result.getMessage().equals("")){
                    respData = "{ \"authToken\": \"" + result.getAuthToken() + "\",\"" +
                            "userName\": \"" + result.getUserName() + "\",\"" +
                            "personID\": \"" + result.getPersonID() + "\" }";
                }else{
                    respData = "{ \"message\": \"" + result.getMessage() + "\"}";
                }

                FillService fs = new FillService();
                fs.fill(new FillRequest(user.getUserName(), 4));

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
