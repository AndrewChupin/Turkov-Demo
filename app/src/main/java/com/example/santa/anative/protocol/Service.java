package com.example.santa.anative.protocol;

import com.example.santa.anative.network.ConnectionManager;

/**
 * Created by santa on 28.02.17.
 */

public class Service {

    private static Service sService;
    private ConnectionManager mConnectionManager;

    private Service() {}

    public static Service getDefault() {
        if (sService == null) sService = new Service();
        return sService;
    }

    public void post(Protocol protocol) {
        if (protocol != null) protocol.onStart();
    }

}

