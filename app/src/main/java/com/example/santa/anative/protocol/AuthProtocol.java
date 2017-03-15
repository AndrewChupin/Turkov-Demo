package com.example.santa.anative.protocol;

import android.os.AsyncTask;
import android.util.Log;

import com.example.santa.anative.application.Configurations;
import com.example.santa.anative.network.Connection;
import com.example.santa.anative.network.ConnectionDelegate;
import com.example.santa.anative.util.algorithm.AlgorithmUtils;
import com.example.santa.anative.util.common.Validate;

/**
 * Created by santa on 28.02.17.
 */

public class AuthProtocol extends AsyncTask<String, String, Void> implements Protocol, ConnectionDelegate {

    private Connection mConnection;
    private Observer mObserver;
    private String email;

    // Server keys
    private static final String CONNECT_MARKER_SERVER = "H";

    // Client keys
    private static final String CONTROL_MARKER_CLIENT = "A";

    // Errors
    public static final int ERROR_SERVER_NAME = 302;



    public AuthProtocol(Connection connection) {
        mConnection = connection;
        mConnection.attachDelegate(this);
    }


    @Override
    protected Void doInBackground(String... params) {
        mConnection.start();
        return null;
    }


    @Override
    public void onStop() {
        mConnection.stopClient();
        mObserver.onComplete();
    }


    @Override
    public void onStart() {
        this.execute();
    }


    @Override
    public void subscribe(Observer observer) {
        mObserver = observer;
    }


    @Override
    public void messageReceived(String response) {
        Log.d("Logos", "AuthProtocol : "+ Thread.currentThread().getName());
        String[] sessionParams = response.split("\\s+");
        switch (sessionParams[0]) {
            case CONNECT_MARKER_SERVER:
                compareServer(sessionParams[1]);
                break;
            default:
                break;
        }
    }

    private void compareServer(String serverName) {
        if (!Validate.isNullOrEmpty(serverName) && serverName.equals(Configurations.SERVER_NAME)) {
            mConnection.sendMessage(generateSessionRequest());
        } else {
            mConnection.stopClient();
            mObserver.onError(ERROR_SERVER_NAME);
        }
    }

    private String generateSessionRequest() {
        String request = CONTROL_MARKER_CLIENT + " " + email + "#" + AlgorithmUtils.generateAbilitiesMask() +
                " " +  AlgorithmUtils.generateSecureRandom();
        return request;
    }

}
