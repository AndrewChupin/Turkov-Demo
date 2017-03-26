package com.example.santa.anative.ui.auth;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionManager;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.service.AuthService;
import com.example.santa.anative.network.service.TokenService;
import com.example.santa.anative.ui.abstarct.Presentable;
import com.example.santa.anative.util.algorithm.RealmSecure;
import com.example.santa.anative.util.network.ServiceError;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 28.02.17.
 * Presenter for AuthView
 * @see AuthActivity
 */

class AuthPresenter implements Presentable {

    private AuthView mAuthView;
    private AuthService mAuthService;
    private TokenService tokenService;
    private Profile mProfile;

    AuthPresenter(AuthView authView) {
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
        mAuthService = new AuthService(connection, mProfile, email, password);

        mAuthService.onSubscribe(new Observer() {
                @Override
                public void onError(int code) {
                    switch (code) {
                        case ServiceError.ERROR_RESPONSE:
                            mAuthView.showError(R.string.incorrect_response);
                            break;
                    }
                    mAuthService.onStop();
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
        mAuthView.showDialog();

        Connection connection = ConnectionManager.getDefault().create(HOST, PORT);
        tokenService = new TokenService(connection);

        tokenService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                switch (code) {
                    case ServiceError.ERROR_RESPONSE:
                        mAuthView.showError(R.string.incorrect_response);
                        break;
                }
                tokenService.onStop();
                mAuthView.hideDialog();
            }

            @Override
            public void onComplete( ) {
                mAuthView.hideDialog();
                mAuthView.onStartMain();
            }
        }).onStart();
    }

}
