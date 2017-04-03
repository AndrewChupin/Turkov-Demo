package com.example.santa.anative.ui.auth;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.connection.ConnectionManager;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.service.AuthService;
import com.example.santa.anative.network.service.TokenService;
import com.example.santa.anative.ui.common.Presentable;
import com.example.santa.anative.util.common.Validate;
import com.example.santa.anative.util.realm.RealmSecure;
import com.example.santa.anative.util.network.ServiceError;

import io.realm.Realm;

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
    private Realm mRealm;

    AuthPresenter(AuthView authView) {
        mAuthView = authView;
    }

    @Override
    public void onCreate() {
        mRealm = RealmSecure.getDefault();
        mProfile = ProfileRepository.getProfile(mRealm);
        if (mProfile.getSessionId().length != 0) {
            onAuthorizeToken();
        }
        else mAuthView.showAuthorizationForm(); 
    }


    @Override
    public void onDestroy() {
        mRealm.close();
    }


    void onAuthorizePassword(String email, byte[] password) {
        if (!Validate.isEmailValid(email)) {
            mAuthView.showMessage(R.string.incorrect_email);
            return;
        }

        mAuthView.showDialog();

        Connection connection = ConnectionManager.getDefault().create(HOST, PORT);
        mAuthService = new AuthService(connection, mProfile, email, password);

        mAuthService.onSubscribe(new Observer() {
                @Override
                public void onError(int code) {
                    switch (code) {
                        case ServiceError.ERROR_RESPONSE:
                            mAuthView.showMessage(R.string.incorrect_response);
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
            });
        tokenService.onStart();
    }


    private void onAuthorizeToken() {
        mAuthView.showDialog();

        Connection connection = ConnectionManager.getDefault().create(HOST, PORT);
        tokenService = new TokenService(connection, mProfile);

        tokenService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                switch (code) {
                    case ServiceError.ERROR_RESPONSE:
                        mAuthView.showMessage(R.string.incorrect_response);
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
        });
        tokenService.onStart();
    }

}
