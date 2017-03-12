package com.example.santa.anative.protocol;

import com.example.santa.anative.application.Configurations;
import com.example.santa.anative.network.Connection;
import com.example.santa.anative.network.ConnectionDelegate;
import com.example.santa.anative.util.common.Validate;

/**
 * Created by santa on 28.02.17.
 */

public class AuthProtocol implements Protocol, ConnectionDelegate {

    private Connection mConnection;

    private static final String SERVER_ID = "H";
    private static final String TAG = "com.example.santa.anative.protocol.AuthProtocol";

    public AuthProtocol(Connection connection) {
        mConnection = connection;
        if (mConnection != null) mConnection.attachDelegate(this);
    }

    @Override
    public void messageReceived(String response) {
        String[] sessionParams = response.split("\\s+");
        switch (sessionParams[0]) {
            case SERVER_ID:
                compareServer(sessionParams[1]);
                break;
            default:
                break;
        }
    }

    private void compareServer(String serverName) {
        if (!Validate.isNullOrEmpty(serverName) && serverName.equals(Configurations.SERVER_NAME)) {
            mConnection.sendMessage(generateOpenSession());
        }
    }

    private String generateOpenSession() {
        return "a";
    }

    @Override
    public void onStart() {
        mConnection.start();
    }

    @Override
    public void onStop() {
        mConnection.stopClient();
    }

}
