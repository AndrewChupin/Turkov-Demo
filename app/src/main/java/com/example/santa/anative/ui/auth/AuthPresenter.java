package com.example.santa.anative.ui.auth;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionManager;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.service.AuthService;
import com.example.santa.anative.ui.common.Presentable;
import com.example.santa.anative.ui.common.View;
import com.example.santa.anative.util.algorithm.RealmSecure;
import com.example.santa.anative.util.network.ServiceError;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 28.02.17.
 */

class AuthPresenter implements Presentable {

    private AuthView mAuthView;
    private AuthService authProtocol;
    private Profile mProfile;

    public AuthPresenter(AuthView authView) {
        mAuthView = authView;
    }

    public void onCreate() {
        ProfileRepository profileRepository = new ProfileRepository(RealmSecure.getDefault());
        mProfile = profileRepository.getProfile();
        if (mProfile.getSessionId().length != 0) {
            onAuthorizeToken();
        }
        else mAuthView.showAuthorizationForm(); 
    }

    void onAuthorizePassword(String email, byte[] password) {
        mAuthView.showDialog();

        Connection connection = ConnectionManager.getDefault().create(HOST, PORT);
        authProtocol = new AuthService(connection, mProfile, email, password);

            authProtocol.onSubscribe(new Observer() {
                @Override
                public void onError(int code) {
                    switch (code) {
                        case ServiceError.ERROR_RESPONSE:
                            mAuthView.showError(R.string.incorrect_response);
                            break;
                    }
                    authProtocol.onStop();
                    mAuthView.hideDialog();
                }

                @Override
                public void onComplete( ) {
                    mAuthView.hideDialog();
                    mAuthView.onStartMain();
                }
            }).onStart();
    }

    void onAuthorizeToken() {

    }

}
