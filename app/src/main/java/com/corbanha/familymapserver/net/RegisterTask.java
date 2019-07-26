package com.corbanha.familymapserver.net;

import android.os.AsyncTask;
import android.widget.Toast;

import com.corbanha.familymapserver.model.User;
import com.corbanha.familymapserver.ui.MainActivity;

public class RegisterTask extends AsyncTask<User, Void, Object[]> {

    public interface RegisterListener {
        void onError(Error e);
        void onComplete(boolean success, String message);
    }

    private RegisterListener listener;
    private String serverHost;
    private int serverPort;

    public RegisterTask(RegisterListener listener, String serverHost, int serverPort){
        this.listener = listener;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    protected Object[] doInBackground(User... users) {
        return ServerProxy.registerUser(serverHost, serverPort, users[0]);
    }

    @Override
    protected void onPostExecute(Object[] s) {
        listener.onComplete((Boolean) s[0], (String) s[1]);
    }
}
