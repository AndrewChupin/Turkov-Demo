package com.example.santa.anative.network.connection;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 16.03.17.
 */

public class ConnectionManager {

    private static ConnectionManager sConnectionManager;

    private ConnectionManager() {  }

    public static ConnectionManager getDefault() {
        if (sConnectionManager == null) sConnectionManager = new ConnectionManager();
        return sConnectionManager;
    }

    public Connection create(String host, int port) {
        return new Connection(HOST, PORT);
    }

}
