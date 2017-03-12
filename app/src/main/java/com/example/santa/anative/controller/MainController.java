package com.example.santa.anative.controller;

import com.example.santa.anative.network.ConnectionManager;
import com.example.santa.anative.protocol.AuthProtocol;
import com.example.santa.anative.protocol.Service;

/**
 * Created by santa on 28.02.17.
 */

public class MainController {

    private ConnectionManager mConnectionManager;

    public void getMessage() {
        Service.getDefault().post(new AuthProtocol(mConnectionManager.create()));
    }

}
