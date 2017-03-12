package com.example.santa.anative.network;

/**
 * Created by santa on 28.02.17.
 */

public class ConnectionManager {

    private static ConnectionManager sConnectionManager;

    private ConnectionManager() {}


    public static ConnectionManager getDefault() {
        if (sConnectionManager == null) sConnectionManager = new ConnectionManager();
        return sConnectionManager;
    }



    public Connection create() {
        return new Connection();
    }

}
