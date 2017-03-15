package com.example.santa.anative.ui.auth;

import android.util.Log;

import com.example.santa.anative.application.Configurations;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.network.Connection;
import com.example.santa.anative.network.ConnectionManager;
import com.example.santa.anative.protocol.Observer;
import com.example.santa.anative.protocol.AuthProtocol;

/**
 * Created by santa on 28.02.17.
 */

public class RegistrationPresenter {

    private RegistrationView mRegistrationView;
    private AuthProtocol authProtocol;

    private static final String HOST = "must@turkov";
    private static final int PORT = 12;

    public void onAuthorize() {

        Connection connection = ConnectionManager.getDefault().create(HOST, PORT);

        AuthProtocol authProtocol = new AuthProtocol(connection);

        try {
            authProtocol.subscribe(new Observer() {
                @Override
                public void onError(int code) {
                    switch (code) {
                        case AuthProtocol.ERROR_SERVER_NAME:
                            Log.d("Logos", "RegistrationPresenter : ERROR_RESPONSE_SIZE");
                            break;
                    }
                }

                @Override
                public void onComplete() {

                }
            });

            authProtocol.onStart();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            System.out.println("asdasda");
        }

    }



}
