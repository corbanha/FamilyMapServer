package com.corbanha.familymapserver.net;

import android.os.AsyncTask;

import com.corbanha.familymapserver.model.User;

public class SignInTask extends AsyncTask<User, Void, Object[]> {

    public interface SignInListener {
        void onError(Error e);
        void onComplete(boolean success, String message);
    }

    private SignInListener listener;
    private String serverHost;
    private int serverPort;

    public SignInTask(SignInListener listener, String serverHost, int serverPort){
        this.listener = listener;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    protected Object[] doInBackground(User... users) {
        return ServerProxy.loginUser(serverHost, serverPort, users[0]);
    }

    @Override
    protected void onPostExecute(Object[] s) {
        listener.onComplete((Boolean) s[0], (String) s[1]);
    }
}