package com.example.santa.anative.ui.auth;

import android.util.Log;

import com.example.santa.anative.R;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.repository.ProfileRepository;
import com.example.santa.anative.network.connection.Connection;
import com.example.santa.anative.network.common.Observer;
import com.example.santa.anative.network.service.AuthService;
import com.example.santa.anative.network.service.TokenService;
import com.example.santa.anative.ui.common.Presenter;
import com.example.santa.anative.util.common.Validate;
import com.example.santa.anative.util.realm.RealmSecure;
import com.example.santa.anative.util.network.ServiceEvent;

import io.realm.Realm;

import static com.example.santa.anative.application.Configurations.HOST;
import static com.example.santa.anative.application.Configurations.PORT;

/**
 * Created by santa on 28.02.17.
 * Presenter for AuthView
 * @see AuthActivity
 */

class AuthPresenter implements Presenter {

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
        if (mProfile.getSessionId() != null) {
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

        Connection connection = new Connection(HOST, PORT);
        mAuthService = new AuthService(connection, mProfile, email, password);

        mAuthService.onSubscribe(new Observer() {
                @Override
                public void onError(int code) {
                    Log.d("Logos", "AuthPresenter | onError | : " + code);
                    handleError(code);
                    mAuthService.onStop();
                    mAuthView.hideDialog();
                }
                @Override
                public void onSuccess( ) {
                    Log.d("Logos", "AuthPresenter | onSuccess | : ");
                    mAuthView.hideDialog();
                    mAuthView.onStartMain();
                }
            });

        mAuthService.execute();
    }


    private void onAuthorizeToken() {
        mAuthView.showDialog();

        Connection connection = new Connection(HOST, PORT);
        tokenService = new TokenService(connection, mProfile);

        tokenService.onSubscribe(new Observer() {
            @Override
            public void onError(int code) {
                switch (code) {
                    case ServiceEvent.ERROR_RESPONSE:
                        mAuthView.showMessage(R.string.incorrect_response);
                        break;
                }
                tokenService.onStop();
                mAuthView.hideDialog();
            }

            @Override
            public void onSuccess( ) {
                mAuthView.hideDialog();
                mAuthView.onStartMain();
            }
        });
        tokenService.execute();
    }


    private void handleError(int code) {
        switch (code) {
            case ServiceEvent.ERROR_RESPONSE:
                mAuthView.showMessage(R.string.incorrect_response);
                break;
            case ServiceEvent.ERROR_SEND_MESSAGE:
                mAuthView.showMessage(R.string.error_sending_data);
                break;
            case ServiceEvent.ERROR_CONNECT:
                mAuthView.showMessage(R.string.connection_error);
                break;
            case ServiceEvent.ERROR_INCORRECT_PASSWORD:
                mAuthView.showMessage(R.string.incorrect_password);
                break;
            case ServiceEvent.ERROR_UNKNOWN:
                mAuthView.showMessage(R.string.unknown_error);
                break;
        }
    }
}
